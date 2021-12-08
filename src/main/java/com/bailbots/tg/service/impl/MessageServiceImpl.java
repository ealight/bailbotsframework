package com.bailbots.tg.service.impl;

import com.bailbots.tg.handler.QuartersBot;
import com.bailbots.tg.service.MessageService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class MessageServiceImpl implements MessageService {
    private final QuartersBot telegramBot;

    public MessageServiceImpl(QuartersBot quartersBot) {
        this.telegramBot = quartersBot;
    }


    @SneakyThrows
    @Override
    public void sendMessage(String text, Long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(telegramId));
        sendMessage.setText(text);

        telegramBot.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public void sendSticker(String sticker, Long telegramId)  {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(String.valueOf(telegramId));
        sendSticker.setSticker(new InputFile(sticker));

        telegramBot.execute(sendSticker);
    }

    @SneakyThrows
    @Override
    public void sendPhoto(String caption, String imageURL, Long telegramId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(telegramId));
        sendPhoto.setPhoto(new InputFile(imageURL));
        sendPhoto.setCaption(caption);

        telegramBot.execute(sendPhoto);
    }

    @SneakyThrows
    @Override
    public void sendStaticKeyboard(String text, ReplyKeyboardMarkup replyKeyboardMarkup, Long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(telegramId));
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(text);

        telegramBot.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public void sendInlineKeyboard(String text, InlineKeyboardMarkup inlineKeyboardMarkup, Long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(telegramId));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(text);

        telegramBot.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public void editInlineKeyboard(Integer messageId, Long telegramId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(String.valueOf(telegramId));
        editMessageReplyMarkup.setMessageId(messageId);

        editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(editMessageReplyMarkup);
    }

    @SneakyThrows
    @Override
    public void sendPhotoWithInlineKeyboard(String caption, String imageURL, Long telegramId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(telegramId));
        sendPhoto.setPhoto(new InputFile(imageURL));
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(sendPhoto);
    }


}
