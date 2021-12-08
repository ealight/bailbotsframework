package com.bailbots.tg.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MessageService {
    void sendMessage(String text, Long telegramId);
    void sendSticker(String sticker, Long telegramId) throws TelegramApiException;
    void sendPhoto(String caption, String imageURL, Long telegramId);
    void sendStaticKeyboard(String text, ReplyKeyboardMarkup replyKeyboardMarkup, Long telegramId);
    void sendInlineKeyboard(String text, InlineKeyboardMarkup inlineKeyboardMarkup, Long telegramId);
    void editInlineKeyboard(Integer messageId, Long telegramId, InlineKeyboardMarkup inlineKeyboardMarkup);
    void sendPhotoWithInlineKeyboard(String caption, String imageURL, Long telegramId, InlineKeyboardMarkup inlineKeyboardMarkup);
}
