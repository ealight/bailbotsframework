package com.bailbots.tg.handler;

import com.bailbots.tg.bpp.BotSelectHandle;
import com.bailbots.tg.config.BotConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class QuartersBot extends SpringWebhookBot {
    private static final Logger LOGGER = LogManager.getLogger(QuartersBot.class);

    private final BotConfiguration botConfiguration;

    public QuartersBot(BotConfiguration botConfiguration, DefaultBotOptions options, SetWebhook setWebhook) {
        super(options, setWebhook);
        this.botConfiguration = botConfiguration;
    }

    public QuartersBot(SetWebhook setWebhook, BotConfiguration botConfiguration) {
        super(setWebhook);
        this.botConfiguration = botConfiguration;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message message = extractMessageFromUpdate(update);

        Long telegramId = message.getChatId();

        if(botConfiguration.getGroupId() == null || botConfiguration.getGroupId().isEmpty()) {
            botConfiguration.setGroupId(String.valueOf(telegramId));
        }

        BotSelectHandle.processByUpdate(update);
        return null;
    }

    @Override
    public String getBotPath() {
        return botConfiguration.getWebhookPath();
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getBotToken();
    }

    private Message extractMessageFromUpdate(Update update) {
        Message message = update.getMessage();

        if (message == null) {
            if (update.getCallbackQuery().getMessage() != null) {
                message = update.getCallbackQuery().getMessage();
            }
            else {
                return null;
            }
        }
        return message;
    }


}
