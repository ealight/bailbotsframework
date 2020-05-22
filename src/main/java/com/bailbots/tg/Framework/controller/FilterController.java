package com.bailbots.tg.Framework.controller;

import com.bailbots.tg.Framework.bpp.annotation.BotController;
import com.bailbots.tg.Framework.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.Framework.service.KeyboardLoaderService;
import com.bailbots.tg.Framework.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class FilterController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public FilterController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("⬅ На главную")
    public void start(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Возвращаемся на главную страницу...",
                keyboardLoaderService.getStaticKeyboardFromXML("start"), telegramId);
    }
}
