package io.github.dbstarll.chat.boot.model.request;

import java.io.Serializable;
import java.util.StringJoiner;

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

    @Override
    public String toString() {
        return new StringJoiner(", ", Question.class.getSimpleName() + "[", "]")
                .add("content='" + content + "'")
                .toString();
    }
}
