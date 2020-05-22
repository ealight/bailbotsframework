package com.bailbots.tg.Framework.controller;

import com.bailbots.tg.Framework.bpp.annotation.BotController;
import com.bailbots.tg.Framework.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.Framework.service.KeyboardLoaderService;
import com.bailbots.tg.Framework.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class StartController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public StartController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("/start")
    public void start(Update update) {
        Long telegramId = update.getMessage().getChatId();

        String text = "Привет, наш бот поможет выбрать вам дом, просто отвечайте на вопросы. "
                + "Что Вы хотите сделать, посмотреть информацию о Славске или выбрать дом?";

        messageService.sendStaticKeyboard(text,
                keyboardLoaderService.getStaticKeyboardFromXML("start"), telegramId);
    }

    @BotRequestMapping("Выбрать дом")
    public void choseHouse(Update update) {
        Long telegramId = update.getMessage().getChatId();

        messageService.sendStaticKeyboard("Хорошо, теперь вы можете выбрать фильтры для поиска:",
                keyboardLoaderService.getStaticKeyboardFromXML("filter"), telegramId);
    }

}
