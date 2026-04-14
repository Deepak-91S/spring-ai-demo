package com.deep_ai.demo.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


////Chat Model is base
//@RestController
//public class OpenAIController {
//
//    private OpenAiChatModel chatModel;
//
//
//    @Autowired
//    public OpenAIController(OpenAiChatModel chatModel){
//        this.chatModel = chatModel;
//    }
//
//
//    @GetMapping("api/{message}")
//    public String getOutput(@PathVariable String message){
//        String result = chatModel.call(message);
//        return result;
//    }
//}

////Chat Client is build on top of chat model
//@RestController
//public class OpenAIController {
//
//    private ChatClient chatClient;
//
////    //If we have multiple models this approach is good we can also do autowiring with other way below when there is only one model(using chatBuilder)
////    @Autowired
////    public OpenAIController(OpenAiChatModel chatModel){
////        this.chatClient = ChatClient.create(chatModel);
////    }
//
//    public OpenAIController(ChatClient.Builder chatBuilder){
//        this.chatClient = chatBuilder.build();
//    }
//
//// // 1. content will just give String
////    @GetMapping("api/{message}")
////    public ResponseEntity<String> getOutput(@PathVariable String message){
////
////        String result = chatClient.
////                prompt(message).
////                call().
////                content();
////
////        return ResponseEntity.ok(result);
////    }
//
//    // // 2. chat Response will have more data
//    @GetMapping("api/{message}")
//    public ResponseEntity<String> getOutput(@PathVariable String message){
//
//        ChatResponse chatResponse = chatClient.
//                prompt(message).
//                call().
//                chatResponse();
//
//        System.out.println(chatResponse.getMetadata().getModel() + chatResponse.getMetadata().getUsage());
//
//        String response = chatResponse.
//                getResult().
//                getOutput().
//                getText();
//
//        return ResponseEntity.ok(response);
//    }
//}



    //Memory Advisor - will keep chat history otherwise LLM wont rememebr the context or history. It wont store
    //Its contuniatution of above,adding memory or advisor
@RestController
@RequestMapping("/api")
public class OpenAIController {

    @Qualifier("openAiChatModel")
    private ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;

//In-memory chat memory autowiring
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

////Advisor

    public OpenAIController(ChatClient.Builder chatBuilder){
        this.chatClient = chatBuilder.
                defaultAdvisors(MessageChatMemoryAdvisor.
                        builder(chatMemory).
                        build()).
                build();
    }

    // // 2. chat Response will have more data
    @GetMapping("/{message}")
    public ResponseEntity<String> getOutput(@PathVariable String message){

        System.out.println("From Open AI --");

        ChatResponse chatResponse = chatClient.
                prompt(message).
                call().
                chatResponse();

        System.out.println(chatResponse.getMetadata().getModel() + chatResponse.getMetadata().getUsage());

        String response = chatResponse.
                getResult().
                getOutput().
                getText();

        return ResponseEntity.ok(response);
    }


    @PostMapping("/recommend")
    public String recommend(@RequestParam String type, @RequestParam String lang, @RequestParam int year){

        String temp = """
                I need the movie of {type} of the {lang} and it should be around {year} 
                
                The response should be in the format:
                Movie name:
                Cast:
                Year:
                IMDB Rating:
                """;

        PromptTemplate promptTemplate = new PromptTemplate(temp);

        Prompt prompt = promptTemplate.create(Map.of("type",type, "lang",lang, "year",year));

        String chatResponse = chatClient.prompt(prompt).call().content();

        return chatResponse;

    }


    @PostMapping("/embedding")
    public float[] embedding(@RequestParam String text){

        return embeddingModel.embed(text);

    }

    @PostMapping("/similarity")
    public double similarity(@RequestParam String text1, @RequestParam String text2){

        float[] embedText1 = embeddingModel.embed(text1);
        float[] embedText2 = embeddingModel.embed(text2);

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for(int i = 0; i< embedText1.length; i++){
            dotProduct += embedText1[i] * embedText2[i];
            norm1 += Math.pow(embedText1[i], 2);
            norm2 += Math.pow(embedText2[i], 2);
        }

        return dotProduct * 100 / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
