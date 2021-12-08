package com.bailbots.tg.config;

import com.bailbots.tg.handler.QuartersBot;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Configuration
@Getter
@Setter
@Slf4j
public class BotConfiguration {

    @Value("${telegram.token}")
    private String botToken;

    @Value("${telegram.name}")
    private String botName;

    @Value("${telegram.groupId}")
    private String groupId;

    @Value("${telegram.webhook.path}")
    private String webhookPath;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(this.webhookPath).build();
    }

    @Bean
    public QuartersBot springWebhookBot(SetWebhook setWebhook) {
        return new QuartersBot(setWebhook, this);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void verifyWebhook() {
        try {
            URL url = new URL(
                    String.format("https://api.telegram.org/bot%s/setWebhook?url=%s", this.botToken, this.getWebhookPath())
            );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.disconnect();
            log.info("Verify webhook {}, Response code {}", url.getHost(), connection.getResponseCode());
        } catch (IOException e) {
            log.error("Ping FAILED");
        }
    }
}
