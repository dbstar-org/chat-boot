package io.github.dbstarll.chat.boot.controller;

import io.github.dbstarll.chat.boot.event.StreamChatEvent;
import io.github.dbstarll.chat.boot.model.request.Question;
import io.github.dbstarll.utils.net.api.ApiException;
import io.github.dbstarll.utils.openai.OpenAiClient;
import io.github.dbstarll.utils.openai.model.api.ChatCompletion;
import io.github.dbstarll.utils.openai.model.api.TextCompletion;
import io.github.dbstarll.utils.openai.model.fragment.Message;
import io.github.dbstarll.utils.openai.model.request.ChatRequest;
import io.github.dbstarll.utils.openai.model.request.CompletionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
class ChatController implements ApplicationEventPublisherAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private static final int DEFAULT_MAX_TOKENS = 512;

    private final OpenAiClient openAiClient;

    private ApplicationEventPublisher applicationEventPublisher;

    ChatController(final OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String indexPage() {
        return "chat.html";
    }

    @PostMapping
    @ResponseBody
    TextCompletion completion(final Question question) throws IOException, ApiException {
        final CompletionRequest request = new CompletionRequest();
        request.setPrompt(question.getContent());
        request.setModel("text-ada-001");
        request.setMaxTokens(DEFAULT_MAX_TOKENS);
        return openAiClient.completion(request);
    }

    @PostMapping("/chat")
    @ResponseBody
    ChatCompletion chat(final Question question) throws IOException, ApiException {
        final ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMaxTokens(DEFAULT_MAX_TOKENS);
        request.setMessages(Collections.singletonList(Message.user(question.getContent())));
        return openAiClient.chat(request);
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    SseEmitter stream(final Question question) {
        LOGGER.info("stream: {}", question);
        final SseEmitter emitter = new SseEmitter();
        applicationEventPublisher.publishEvent(new StreamChatEvent(question, emitter));
        return emitter;
    }
}
