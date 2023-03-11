package io.github.dbstarll.chat.boot;

import io.github.dbstarll.utils.openai.OpenAiClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
