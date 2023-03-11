package io.github.dbstarll.chat.boot;

import io.github.dbstarll.utils.spring.boot.BootLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Starter extends BootLauncher {
    /**
     * 启动类.
     *
     * @param args 命令行参数
     */
    public static void main(final String[] args) {
        new Starter().run(null, "chat-boot", args);
    }
}
