package com.bailbots.tg.controller;

import com.bailbots.tg.bpp.annotation.BotController;
import com.bailbots.tg.bpp.annotation.BotRequestMapping;
import com.bailbots.tg.model.Event;
import com.bailbots.tg.model.Sticker;
import com.bailbots.tg.repository.StickerRepository;
import com.bailbots.tg.service.EventsService;
import com.bailbots.tg.service.KeyboardLoaderService;
import com.bailbots.tg.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@BotController
public class StartController {
    private static final String START_COMMAND = "/start";
    private static final String ADD_EVENT_COMMAND = "/add";
    private static final String ALL_EVENTS_COMMAND = "/all";
    private static final String EVENTS_TODAY_COMMAND = "/today";
    private static final String ADD_STICKER_COMMAND = "/addsticker";
    private static final String DELETE_STICKER_COMMAND = "/removesticker";
    private static final String WEEKEND_COMMAND = "/weekend";

    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final EventsService eventsService;
    private final StickerRepository stickerRepository;

    public StartController(MessageService messageService, KeyboardLoaderService keyboardLoaderService, EventsService eventsService, StickerRepository stickerRepository) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.eventsService = eventsService;
        this.stickerRepository = stickerRepository;
    }

    @BotRequestMapping(START_COMMAND)
    public void start(Update update) {
        Long telegramId = update.getMessage().getChatId();
        Random rand = new Random();
        List<String> names = List.of("Саша", "Віка", "Неля", "Максим", "Денис", "Вова", "Коля", "ЖЕКА", "Гома");

        String randomName = names.get(rand.nextInt(names.size()));

        messageService.sendStaticKeyboard("Зараз я " + randomName + " \uD83E\uDD16",
                keyboardLoaderService.getStaticKeyboardFromXML("start"), telegramId);
    }

    @BotRequestMapping(ADD_EVENT_COMMAND)
    public void choseHouse(Update update) {
        eventsService.addEvent(update);
    }

    @BotRequestMapping(ALL_EVENTS_COMMAND)
    public void allEvents(Update update) {
        showEvents(update, eventsService.getAllEvents());
    }

    @BotRequestMapping(EVENTS_TODAY_COMMAND)
    public void today(Update update) {
        showEvents(update, eventsService.getAllEventsToday());
    }

    @BotRequestMapping("Всі івенти")
    public void allEventsText(Update update) {
        showEvents(update, eventsService.getAllEvents());
    }

    @BotRequestMapping("Івенти сьогодні")
    public void todayText(Update update) {
        showEvents(update, eventsService.getAllEventsToday());
    }

    @BotRequestMapping("Додати івент")
    public void addEvent(Update update) {
        Long chatId = update.getMessage().getChatId();
        messageService.sendMessage("Введіть: /add [Назва] [Час, пр: 14:00] [Дата, пр: 22.09.2005]", chatId);
    }

    @BotRequestMapping(WEEKEND_COMMAND)
    public void weekend(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String time = text.substring(text.indexOf(WEEKEND_COMMAND) + WEEKEND_COMMAND.length());

        if (time.isEmpty()) {
            eventsService.getWeekendTime();
        } else {
            eventsService.setWeekendTime(time);
        }

        messageService.sendMessage(String.format("Час засетаний на %s", time), chatId);
    }

    @BotRequestMapping(ADD_STICKER_COMMAND)
    public void addSticker(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String url = text.substring(text.indexOf(ADD_STICKER_COMMAND) + ADD_STICKER_COMMAND.length());

        Sticker sticker = Sticker.builder()
                .url(url)
                .build();

        stickerRepository.save(sticker);

        messageService.sendMessage(String.format(
                "Новий стікер додано: ID: %s | URL: %s", sticker.getId(), sticker.getUrl()
        ), chatId);
    }

    @BotRequestMapping(DELETE_STICKER_COMMAND)
    public void removeSticker(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String id = text.substring(text.indexOf(DELETE_STICKER_COMMAND) + DELETE_STICKER_COMMAND.length());

        Optional<Sticker> sticker = stickerRepository.findById(Long.valueOf(id));

        sticker.ifPresentOrElse(stickerRepository::delete, () -> messageService.sendMessage(
                String.format("Стікера з ID %s не існує", id), chatId));
    }

    private void showEvents(Update update, List<Event> events) {
        Long chatId = update.getMessage().getChatId();
        StringBuilder builder = new StringBuilder();

        if (events.isEmpty()) {
            builder.append("Немає :(");
        } else {
            events
                    .forEach(event -> builder.append(event.toString()));
        }

        messageService.sendMessage(builder.toString(), chatId);
    }
}

