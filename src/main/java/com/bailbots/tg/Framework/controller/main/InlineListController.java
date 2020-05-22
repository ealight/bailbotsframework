package com.bailbots.tg.Framework.controller.main;

import com.bailbots.tg.Framework.bpp.annotation.BotController;
import com.bailbots.tg.Framework.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.Framework.service.KeyboardItemService;
import com.bailbots.tg.Framework.service.KeyboardLoaderService;
import com.bailbots.tg.Framework.service.MessageService;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController("InlineList")
public class InlineListController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final KeyboardItemService keyboardItemService;

    public InlineListController(MessageService messageService, KeyboardLoaderService keyboardLoaderService, KeyboardItemService keyboardItemService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.keyboardItemService = keyboardItemService;
    }

    @SneakyThrows
    @BotRequestMapping("GetItem")
    public void choseItem(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();

        String entity = callbackQuery.getData().split("/")[2].trim();
        String methodByEntity = entity.substring(0, 1).toLowerCase() + entity.substring(1);

        Long id = Long.parseLong(callbackQuery.getData().split("/")[3].trim());

        keyboardItemService.getClass().getDeclaredMethod(methodByEntity, Long.class, Long.class)
                .invoke(keyboardItemService, telegramId, id);
    }

    @BotRequestMapping("Next")
    public void next(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String callbackData = callbackQuery.getData();

        int maxPage = Integer.parseInt(callbackQuery.getData().split("/")[3].trim());
        String keyboardName = callbackQuery.getData().split("/")[4].trim();


        messageService.editInlineKeyboard(messageId, telegramId,
                keyboardLoaderService.getInlineListFromXML(keyboardName, setNextPage(maxPage, callbackData)));

    }

    @BotRequestMapping("Previous")
    public void previous(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        Long telegramId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String callbackData = callbackQuery.getData();

        int maxPage = Integer.parseInt(callbackQuery.getData().split("/")[3].trim());
        String keyboardName = callbackQuery.getData().split("/")[4].trim();

        messageService.editInlineKeyboard(messageId, telegramId,
                keyboardLoaderService.getInlineListFromXML(keyboardName, setPreviousPage(maxPage, callbackData)));

    }

    private int setPreviousPage(int maxPage, String callbackData) {
        int page = Integer.parseInt(callbackData.split("/")[2].trim());

        page = page - 1;

        if (page == -1) {
            page = maxPage - 1;
        }
        return page;
    }

    private int setNextPage(int maxPage, String callbackData) {
        int page = Integer.parseInt(callbackData.split("/")[2].trim());

        page = page + 1;
        if (page == maxPage) {
            page = 0;
        }
        return page;
    }
}
