package io.github.dbstarll.chat.boot;

import io.github.dbstarll.chat.boot.model.request.Prompt;
import io.github.dbstarll.utils.net.api.ApiException;
import io.github.dbstarll.utils.openai.OpenAiClient;
import io.github.dbstarll.utils.openai.model.api.ChatCompletion;
import io.github.dbstarll.utils.openai.model.api.TextCompletion;
import io.github.dbstarll.utils.openai.model.fragment.Message;
import io.github.dbstarll.utils.openai.model.request.ChatRequest;
import io.github.dbstarll.utils.openai.model.request.CompletionRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
class ChatController {
    private final OpenAiClient openAiClient;

    ChatController(final OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String indexPage() {
        return "chat.html";
    }

    @PostMapping
    @ResponseBody
    TextCompletion completions(final Prompt prompt) throws IOException, ApiException {
        final CompletionRequest request = new CompletionRequest();
        request.setPrompt(prompt.getPrompt());
        request.setModel("text-ada-001");
        request.setMaxTokens(1024);
        return openAiClient.completions(request);
    }

    @PostMapping("/chat")
    @ResponseBody
    ChatCompletion chat(final Prompt prompt) throws IOException, ApiException {
        final ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMaxTokens(1024);
        request.setMessages(Collections.singletonList(Message.user(prompt.getPrompt())));
        return openAiClient.chat(request);
    }
}
