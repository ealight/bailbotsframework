package com.bailbots.tg.controller;

import com.bailbots.tg.bpp.annotation.BotController;
import com.bailbots.tg.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.model.Event;
import com.bailbots.tg.service.EventsService;
import com.bailbots.tg.service.KeyboardLoaderService;
import com.bailbots.tg.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Random;
import java.util.Set;

@BotController
public class StartController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final EventsService eventsService;

    public StartController(MessageService messageService, KeyboardLoaderService keyboardLoaderService, EventsService eventsService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.eventsService = eventsService;
    }


    @BotRequestMapping("/start")
    public void start(Update update) {
        Long telegramId = update.getMessage().getChatId();
        Random rand = new Random();
        List<String> names = List.of("Саша", "Віка", "Неля", "Максим", "Денис", "Вова", "Коля", "ЖЕКА", "Гома");

        String randomName = names.get(rand.nextInt(names.size()));

        messageService.sendStaticKeyboard("Зараз я " + randomName + " \uD83E\uDD16",
                keyboardLoaderService.getStaticKeyboardFromXML("start"), telegramId);
    }

    @BotRequestMapping("/addEvent")
    public void choseHouse(Update update) {
        eventsService.addEvent(update);
    }

    @BotRequestMapping("/allEvents")
    public void allEvents(Update update) {
        showEvents(update, eventsService.getAllEvents());
    }

    @BotRequestMapping("Всі івенти")
    public void allEventsText(Update update) {
        showEvents(update, eventsService.getAllEvents());
    }

    @BotRequestMapping("/todayEvents")
    public void today(Update update) {
        showEvents(update, eventsService.getAllEventsToday());
    }

    @BotRequestMapping("Івенти сьогодні")
    public void todayText(Update update) {
        showEvents(update, eventsService.getAllEventsToday());
    }

    @BotRequestMapping("Додати івент")
    public void addEvent(Update update) {
        Long chatId = update.getMessage().getChatId();
        messageService.sendMessage("Введіть: /addEvent [Назва] [Час, пр: 14:00] [Дата, пр: 22.09.2005]", chatId);
    }

    @BotRequestMapping("/weekend")
    public void weekend(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String time = text.substring(text.indexOf("/weekend") + 8);

        eventsService.setWeekendTime(time);

        messageService.sendMessage(String.format("Час засетаний на %s", time), chatId);
    }

    private void showEvents(Update update, List<Event> events) {
        Long chatId = update.getMessage().getChatId();
        StringBuilder builder = new StringBuilder();

        if(events.isEmpty()) {
            builder.append("Немає :(");
        }
        else {
            events
                    .forEach(event -> builder.append(event.toString()));
        }

        messageService.sendMessage(builder.toString(), chatId);
    }
}

