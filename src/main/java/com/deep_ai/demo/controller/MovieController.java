package com.deep_ai.demo.controller;


import com.deep_ai.demo.config.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Qualifier("openAiChatModel")
    private ChatClient chatClient;

    public MovieController(OpenAiChatModel chatModel){
        this.chatClient = ChatClient.create(chatModel);
    }

    @PostMapping("/listofmovies")
    public List<String> getListOfMovies(@RequestParam String actor){


        String message = """
                List Top 5 movies of {actor}
                {format}
                """;

        ListOutputConverter list = new ListOutputConverter(new DefaultConversionService());


        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of(
                "actor", actor,
                "format", list.getFormat()
        ));
        List<String> movies = list.convert(chatClient.prompt(prompt).call().content());

        return movies;
    }

    @PostMapping("/movie")
    public Movie getMovieData(@RequestParam String actor){
        String message = """
        Give only one best movie of {actor}
        Return exactly one JSON object
        {format}
        """;

        BeanOutputConverter<Movie> bean = new BeanOutputConverter<>(Movie.class);

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of(
                "actor", actor,
                "format", bean.getFormat()
        ));
        Movie movie = bean.convert(chatClient.prompt(prompt).call().content());

        return movie;

    }

    @PostMapping("/moviesList")
    public List<Movie> getListOfMoviesData(@RequestParam String actor){
        String message = """
                List Top 5 movies of {actor}
                {format}
                """;

        BeanOutputConverter<List<Movie>> bean = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<Movie>>() {
        });

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of(
                "actor", actor,
                "format", bean.getFormat()
        ));
        List<Movie> movies = bean.convert(chatClient.prompt(prompt).call().content());

        return movies;

    }

}
