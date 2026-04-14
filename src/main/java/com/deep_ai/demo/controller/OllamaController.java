package com.deep_ai.demo.controller;

//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//public class OllamaController {
//
//
//
//        private ChatClient chatClient;
//
//
//
//        //Advisor
//        public OllamaController(OllamaChatModel ollamaChatModel){
//            this.chatClient = ChatClient.create(ollamaChatModel);
//        }
//
//        // // 2. chat Response will have more data
//        @GetMapping("api/{message}")
//        public ResponseEntity<String> getOutput(@PathVariable String message){
//
//            ChatResponse chatResponse = chatClient.
//                    prompt(message).
//                    call().
//                    chatResponse();
//
//            System.out.println(chatResponse.getMetadata().getModel() + chatResponse.getMetadata().getUsage());
//
//            String response = chatResponse.
//                    getResult().
//                    getOutput().
//                    getText();
//
//            return ResponseEntity.ok(response);
//        }
//
//}
