package io.github.dbstarll.chat.boot.model.request;

import java.io.Serializable;

public final class Prompt implements Serializable {
    private String prompt;

    /**
     * get prompt.
     *
     * @return prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * set prompt.
     *
     * @param prompt prompt
     */
    public void setPrompt(final String prompt) {
        this.prompt = prompt;
    }
}
