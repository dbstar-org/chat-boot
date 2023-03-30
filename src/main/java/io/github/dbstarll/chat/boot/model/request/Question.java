package io.github.dbstarll.chat.boot.model.request;

import java.io.Serializable;

public final class Question implements Serializable {
    private String content;

    /**
     * get content.
     *
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * set content.
     *
     * @param content content
     */
    public void setContent(final String content) {
        this.content = content;
    }
}
