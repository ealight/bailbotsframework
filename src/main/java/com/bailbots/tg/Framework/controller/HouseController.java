package com.bailbots.tg.Framework.controller;

import com.bailbots.tg.Framework.bpp.annotation.BotController;
import com.bailbots.tg.Framework.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.Framework.dao.House;
import com.bailbots.tg.Framework.dao.HouseImageData;
import com.bailbots.tg.Framework.repository.HouseRepository;
import com.bailbots.tg.Framework.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController("House")
public class HouseController {
    private final HouseRepository houseRepository;
    private final MessageService messageService;

    public HouseController(HouseRepository houseRepository, MessageService messageService) {
        this.houseRepository = houseRepository;
        this.messageService = messageService;
    }

    @BotRequestMapping("MorePhoto")
    public void morePhoto(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split("/")[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        for(HouseImageData image : house.getImages()) {
            messageService.sendPhoto("", image.getUrl(), telegramId);
        }
    }

    @BotRequestMapping("Detail")
    public void detail(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split("/")[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        String detailInformation = "Дом #" + house.getId()
                + "\n\n"
                + "Детальнее: \n" + house.getDetailInfo();

        messageService.sendMessage(detailInformation, telegramId);
    }

    @BotRequestMapping("OwnerRequirements")
    public void ownerRequirements(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split("/")[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        String detailInformation = "Дом #" + house.getId()
                + "\n\n"
                + "Требования владельца: \n" + house.getOwnerRequirements();

        messageService.sendMessage(detailInformation, telegramId);
    }

    @BotRequestMapping("AdditionalService")
    public void additionalService(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long id = Long.parseLong(callbackQuery.getData().split("/")[2].trim());

        House house = houseRepository.getById(id);
        Long telegramId = callbackQuery.getMessage().getChatId();

        String detailInformation = "Дом #" + house.getId()
                + "\n\n"
                + "Дополнительные услуги: \n" + house.getAdditionalService();

        messageService.sendMessage(detailInformation, telegramId);
    }
}
