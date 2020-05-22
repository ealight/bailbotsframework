package com.bailbots.tg.Framework.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public interface KeyboardLoaderService {
    ReplyKeyboardMarkup getStaticKeyboardFromXML(String filename);
    InlineKeyboardMarkup getInlineKeyboardFromXML(String filename, Long ...itemId);
    InlineKeyboardMarkup getInlineListFromXML(String filename, Integer page);
}
