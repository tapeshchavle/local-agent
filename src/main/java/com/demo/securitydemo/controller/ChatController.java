package com.demo.securitydemo.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String answer = chatClient.prompt()
                .system("You are a helpful AI assistant.")
                .user(request.message())
                .call()
                .content();

        return new ChatResponse(answer);
    }
    @GetMapping("/chat-hi")
    public ChatResponse chat() {

        String answer = chatClient.prompt()
                .system("You are a helpful AI assistant.")
                .user("Explain about the java in details")
                .call()
                .content();

        return new ChatResponse(answer);
    }

    public record ChatRequest(String message) {}

    public record ChatResponse(String answer) {}
}

