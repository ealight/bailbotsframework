package com.bailbots.tg.Framework.service.impl;

import com.bailbots.tg.Framework.dao.House;
import com.bailbots.tg.Framework.repository.HouseRepository;
import com.bailbots.tg.Framework.service.KeyboardItemService;
import com.bailbots.tg.Framework.service.KeyboardLoaderService;
import com.bailbots.tg.Framework.service.MessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class KeyboardItemServiceImpl implements KeyboardItemService {
    private final MessageService messageService;
    private final HouseRepository houseRepository;
    private final KeyboardLoaderService keyboardLoaderService;

    public KeyboardItemServiceImpl(MessageService messageService, HouseRepository houseRepository, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.houseRepository = houseRepository;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @Override
    public void house(Long telegramId, Long houseId) {
        House house = houseRepository.getById(houseId);

        String image = house.getImages().get(0).getUrl();

        String caption = "Дом #" + house.getId() + "\n\n"
                + "Название: " + house.getName() + "\n"
                + "Описание: " + house.getDescription()  + "\n"
                + "Номер владельца: " + house.getOwnerPhoneNumber();

        InlineKeyboardMarkup keyboard = keyboardLoaderService.getInlineKeyboardFromXML("houseItem", houseId);

        messageService.sendPhotoWithInlineKeyboard(caption, image, telegramId, keyboard);
    }
}
