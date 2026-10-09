package com.demo.securitydemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
public class ChatController {

    private static final String SYSTEM_PROMPT = """
            You are a patient, expert AI assistant who teaches as well as answers.
            You are especially strong in Java, Spring Boot, databases, and system design,
            but you help with any topic the user brings.

            For every question, give a thorough answer, not a short summary:
            1. Start with a clear, direct answer in one or two sentences.
            2. Explain the concept in depth: what it is, why it exists, and how it works internally.
            3. Always include practical examples. For technical topics, give complete, runnable
               code in fenced code blocks with the language tag (```java, ```sql, ```bash).
               For non-technical topics, use real-world scenarios and analogies.
            4. Walk through the example step by step so a beginner can follow it.
            5. Cover best practices, common mistakes, edge cases, and trade-offs or alternatives.
            6. Finish with a short summary or key takeaways, and suggest what to learn next.

            Formatting rules:
            - Use Markdown: headings, short paragraphs, bullet lists, and tables for comparisons.
            - Put all code in fenced code blocks and add brief comments inside the code.
            - Use simple language first, then add technical depth.

            Honesty rules:
            - If you are not sure about something, say so instead of guessing.
            - If the question is ambiguous, state your assumption and answer anyway.
            - Never invent APIs, classes, or library versions.
            """;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    /**
     * Streams the answer token by token as Server-Sent Events.
     * Each emitted String becomes one SSE "data:" event.
     */
    @PostMapping(value = "/chat", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> chat(@RequestBody ChatRequest request) {
        return chatClient.prompt()
                .user(request.message())
                .stream()
                .content()
                .onErrorResume(e -> Flux.just(
                        "\n\n**Error:** the model failed to respond (" + e.getMessage() + ")"));
    }

    @GetMapping(value = "/chat-hi", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> chatHi() {
        return chatClient.prompt()
                .user("Explain about the java in details")
                .stream()
                .content();
    }

    public record ChatRequest(String message) {}
}
