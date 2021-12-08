package com.bailbots.tg.service.impl;

import com.bailbots.tg.parser.inline.InlineButton;
import com.bailbots.tg.parser.inline.InlineDbKeyrow;
import com.bailbots.tg.parser.inline.InlineKeyboard;
import com.bailbots.tg.parser.inline.InlineKeyrow;
import com.bailbots.tg.parser.unchanged.StaticButton;
import com.bailbots.tg.parser.unchanged.StaticKeyboard;
import com.bailbots.tg.parser.unchanged.StaticKeyrow;
import com.bailbots.tg.repository.KeyboardObjectRepository;
import com.bailbots.tg.service.KeyboardLoaderService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeyboardLoaderServiceImpl implements KeyboardLoaderService {
    private static final String STATIC_KEYBOARD_PATH = "src/main/resources/keyboard/static/";
    private static final String INLINE_KEYBOARD_PATH = "src/main/resources/keyboard/inline/";
    private static final String KEYBOARD_DAO_PATH = "com.bailbots.tg.model.";

    private final KeyboardObjectRepository keyboardObjectRepository;

    @Autowired
    public KeyboardLoaderServiceImpl(KeyboardObjectRepository keyboardObjectRepository) {
        this.keyboardObjectRepository = keyboardObjectRepository;
    }

    @Override
    @SneakyThrows
    public ReplyKeyboardMarkup getStaticKeyboardFromXML(String filename) {
        XmlMapper xmlMapper = new XmlMapper();


        StaticKeyboard keyboard = xmlMapper.readValue(
                new File(STATIC_KEYBOARD_PATH + filename + ".xml"), StaticKeyboard.class);

        ReplyKeyboardMarkup result = new ReplyKeyboardMarkup();
        result.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (StaticKeyrow keyrow : keyboard.getKeyRows()) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (StaticButton button : keyrow.getButtons()) {
                keyboardRow.add(new KeyboardButton(button.getText()));
            }
            keyboardRows.add(keyboardRow);
        }

        result.setKeyboard(keyboardRows);

        return result;
    }

    @Override
    @SneakyThrows
    public InlineKeyboardMarkup getInlineKeyboardFromXML(String filename, Long... itemId) {
        XmlMapper mapper = new XmlMapper();

        InlineKeyboard inlineKeyboard = mapper.readValue(
                new File(INLINE_KEYBOARD_PATH + filename + ".xml"), InlineKeyboard.class);

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (inlineKeyboard.getKeyrows() != null) {

            for (InlineKeyrow keyrow : inlineKeyboard.getKeyrows()) {
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

                for (InlineButton button : keyrow.getButtons()) {
                    keyboardRow.add(new InlineKeyboardButton(button.getText())
                            .setCallbackData(inlineKeyboard.getController() + "/"
                                    + button.getCallback() + "/"
                                    + itemId[0]));
                }

                keyboardRows.add(keyboardRow);
            }

        }
        result.setKeyboard(keyboardRows);
        return result;
    }

    @Override
    @SneakyThrows
    public InlineKeyboardMarkup getInlineListFromXML(String filename, Integer page) {
        XmlMapper mapper = new XmlMapper();

        InlineKeyboard inlineKeyboard = mapper.readValue(
                new File(INLINE_KEYBOARD_PATH + filename + ".xml"), InlineKeyboard.class);

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (inlineKeyboard.getDbKeyrows() != null) {

            for (InlineDbKeyrow keyrow : inlineKeyboard.getDbKeyrows()) {
                int maxItemsOnPage = Integer.parseInt(keyrow.getMaxItemsOnPage());
                String entity = keyrow.getEntity();

                Class<?> clazz = Class.forName(KEYBOARD_DAO_PATH + entity);

                Method nameMethod = clazz.getMethod(keyrow.getMethodForName());
                Method callbackMethod = clazz.getMethod(keyrow.getMethodForCallback());

                for (Object object : keyboardObjectRepository.getObjectListByEntity(clazz, page, maxItemsOnPage)) {
                    keyboardRows.add(Collections.singletonList(
                            new InlineKeyboardButton(nameMethod.invoke(object).toString())
                                    .setCallbackData(inlineKeyboard.getController() + "/"
                                            + "GetItem" + "/"
                                            + keyrow.getEntity() + "/"
                                            + callbackMethod.invoke(object).toString())));
                }
            }
        }

        if (inlineKeyboard.getKeyrows() != null) {

            for (InlineKeyrow keyrow : inlineKeyboard.getKeyrows()) {
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

                for (InlineButton button : keyrow.getButtons()) {
                    if (page != -1) {
                        if (button.isPageable()) {
                            button.setText(button.getText() + " " + (page + 1) + "/" + getPageNumbersOfObjectList(filename));
                        }

                        if (!button.getCallback().equals("none")) {
                            button.setCallback(inlineKeyboard.getController() + "/"
                                    + button.getCallback() + "/"
                                    + page + "/"
                                    + getPageNumbersOfObjectList(filename) + "/"
                                    + filename);
                        } else {
                            button.setCallback(inlineKeyboard.getController() + "/"
                                    + button.getCallback());
                        }
                    }
                    keyboardRow.add(new InlineKeyboardButton(button.getText())
                            .setCallbackData(button.getCallback()));
                }

                keyboardRows.add(keyboardRow);
            }

        }
        result.setKeyboard(keyboardRows);
        return result;
    }

    @SneakyThrows
    private int getPageNumbersOfObjectList(String filename) {
        XmlMapper mapper = new XmlMapper();

        InlineKeyboard inlineKeyboard = mapper.readValue(
                new File(INLINE_KEYBOARD_PATH + filename + ".xml"), InlineKeyboard.class);

        int result = 0;

        if (inlineKeyboard.getDbKeyrows() != null) {

            for (InlineDbKeyrow keyrow : inlineKeyboard.getDbKeyrows()) {
                int maxItemsOnPage = Integer.parseInt(keyrow.getMaxItemsOnPage());

                String entity = keyrow.getEntity();

                Class<?> clazz = Class.forName(KEYBOARD_DAO_PATH + entity);

                result = keyboardObjectRepository.getPageNumbersOfObjectListByEntity(clazz, maxItemsOnPage);
            }
        }
        return result;
    }
}
