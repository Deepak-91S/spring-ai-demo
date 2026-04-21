package com.deep_ai.demo.controller;


import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/audio")
public class AudioGenController {

    private OpenAiAudioTranscriptionModel audioTranscriptionModel;

    private OpenAiAudioSpeechModel audioSpeechModel;

    public AudioGenController(OpenAiAudioTranscriptionModel transcriptionModel, OpenAiAudioSpeechModel speechModel){
        this.audioTranscriptionModel = transcriptionModel;
        this.audioSpeechModel = speechModel;
    }

    @PostMapping("/speechtotext")
    public String SpeechToText(@RequestParam MultipartFile file){

        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.
                builder().
                language("ta").
                responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT).
                build();

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource(), options);

        return audioTranscriptionModel.call(prompt).getResult().getOutput();
    }

    @PostMapping("/texttospeech")
    public byte[] textToSpeech(@RequestParam String text){

        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder().
                speed(1.5).voice(OpenAiAudioApi.SpeechRequest.Voice.SHIMMER).build();

        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text, options);

        return audioSpeechModel.call(text);
    }
}
