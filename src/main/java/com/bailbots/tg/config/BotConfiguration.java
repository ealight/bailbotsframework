package com.bailbots.tg.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class BotConfiguration {

    @Value("${telegram.token}")
    private String botToken;

    @Value("${telegram.name}")
    private String botName;

    @Value("${telegram.groupId}")
    private String groupId;
}
