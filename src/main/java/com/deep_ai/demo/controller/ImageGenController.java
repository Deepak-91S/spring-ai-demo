package com.deep_ai.demo.controller;


import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageGenController {

    private ChatClient chatClient;
    private OpenAiImageModel imageModel;

    public ImageGenController(OpenAiImageModel imageModel, OpenAiChatModel chatModel){
        this.chatClient = ChatClient.create(chatModel);
        this.imageModel = imageModel;
    }

    @GetMapping("/{query}")
    public String GenImage(@PathVariable String query){

        ImagePrompt imagePrompt = new ImagePrompt(query, OpenAiImageOptions.builder().
                quality("standard").style("natural").height(1024)
        .build());

        ImageResponse imageResponse = imageModel.call(imagePrompt);

        return imageResponse.getResult().getOutput().getUrl();
    }

    @PostMapping("/describe")
    public String GenImageDescriber(@RequestParam String query, @RequestParam MultipartFile file){

        return chatClient.
                prompt().
                user(us->us.text(query).
                        media(MimeTypeUtils.
                                IMAGE_JPEG, file.
                                getResource())).
                call().
                content();

    }
}
