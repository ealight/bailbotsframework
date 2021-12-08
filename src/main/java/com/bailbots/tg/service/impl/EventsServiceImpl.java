package com.bailbots.tg.service.impl;

import com.bailbots.tg.config.BotConfiguration;
import com.bailbots.tg.model.Event;
import com.bailbots.tg.repository.EventRepository;
import com.bailbots.tg.repository.StickerRepository;
import com.bailbots.tg.service.EventsService;
import com.bailbots.tg.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {
    @Value("${weekend}")
    private String weekendTime;

    private final MessageService messageService;
    private final BotConfiguration botConfiguration;
    private final EventRepository eventRepository;
    private final StickerRepository stickerRepository;

    @Autowired
    public EventsServiceImpl(MessageService messageService, BotConfiguration botConfiguration, EventRepository eventRepository, StickerRepository stickerRepository) {
        this.messageService = messageService;
        this.botConfiguration = botConfiguration;
        this.eventRepository = eventRepository;
        this.stickerRepository = stickerRepository;
    }

    @SneakyThrows
    @Override
    public void addEvent(Update update) {
        Long chatId = update.getMessage().getChatId();

        try {
            User user = update.getMessage().getFrom();
            Date currentDate = new Date();

            Pattern pattern = Pattern.compile("\\[(.*?)\\]");
            List<Optional<String>> args = pattern.matcher(update.getMessage().getText()).results()
                    .map(matchResult -> Optional.of(matchResult.group(1)))
                    .collect(Collectors.toList());

            String name = args.get(0).orElse("");
            Optional<String> time = args.get(1);
            String date = args.size() == 2 ? "" : args.get(2).orElse("");

            if (!time.isPresent()) {
                messageService.sendMessage("Ти не написав час", chatId);
                return;
            }

            if (!time.get().matches("\\d{0,2}:\\d{0,2}")) {
                messageService.sendMessage("Час в форматі hh:mm", chatId);
                return;
            }

            Date dateTime = stringTimeToDate(time.get());

            if (dateTime.getTime() - currentDate.getTime() < 0 && date.isEmpty()) {
                messageService.sendMessage("На вчора плануєш?", chatId);
                return;
            }

            Event event = Event.builder()
                    .name(name)
                    .time(time.get())
                    .notification(false)
                    .date(dateTime)
                    .authorFirstName(user.getFirstName())
                    .authorLastName(user.getLastName())
                    .build();

            if (!date.isEmpty()) {
                Date parsedDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);

                if (parsedDate.before(currentDate)) {
                    messageService.sendMessage("Ти на коли дату поставив??", chatId);
                    return;
                }

                event.setDate(parsedDate);
            }

            eventRepository.save(event);

            messageService.sendMessage(String.format("%s зробив(ла \uD83D\uDC60) подію %s",
                    event.getAuthorFirstName(), event.getName()), chatId);
        } catch (Exception e) {
            messageService.sendMessage("Щось пішло не так, я не можу додати івент \uD83D\uDE30" +
                    "\nПопробуй в форматі /addEvent [Назва] [Час] [Дата] (якшо на сьогодні, дату не пиши)", chatId);
        }
    }

    @Override
    public List<Event> getAllEventsToday() {
        return eventRepository.findAllByOrderByDateAsc().stream()
                .filter(event -> event.getDate().getDay() == new Date().getDay())
                .filter(event -> event.getDate().after(new Date()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByDateAsc().stream()
                .filter(event -> event.getDate().after(new Date()))
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 60 * 1000 * 15)
    public void eventsNotification() {
        log.info("Events notifications worked {}", new Date());
        eventRepository.findAll().stream()
                .filter(event -> event.getDate().getTime() - 60 * 1000 * 60 <= new Date().getTime())
                .filter(event -> !event.getNotification())
                .forEach(event -> {
                            long dateDiff = event.getDate().getTime() - new Date().getTime();
                            String diff = new SimpleDateFormat("mm").format(new Date(dateDiff));

                            messageService.sendMessage(
                                    String.format("Через %s хв. %s який організував(ла \uD83D\uDC60) %s",
                                            diff, event.getName(), event.getAuthorFirstName()),
                                    Long.valueOf(botConfiguration.getGroupId())
                            );

                            event.setNotification(true);
                            eventRepository.save(event);

                            try {
                                messageService.sendSticker(
                                        stickerRepository.getRandomSticker().getUrl(),
                                        Long.valueOf(botConfiguration.getGroupId())
                                );
                            } catch (TelegramApiException e) {
                                log.debug(e.getMessage());
                            }

                            log.info("Notification: {} | Time: {} | Time diff: {}", event.getName(), event.getDate(), diff);
                        }
                );
    }

    @Override
    @Scheduled(cron = "0 0 18 * * FRI,SAT,SUN")
    public void notificationWeekend() {
        messageService.sendMessage(
                String.format("\uD83E\uDD2D Сьогодні на %s", weekendTime), Long.valueOf(botConfiguration.getGroupId())
        );

        try {
            messageService.sendSticker(
                    stickerRepository.getRandomSticker().getUrl(),
                    Long.valueOf(botConfiguration.getGroupId())
            );
        } catch (TelegramApiException e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public void setWeekendTime(String time) {
        this.weekendTime = time;
    }

    @Override
    public String getWeekendTime() {
        return this.weekendTime;
    }

    @SneakyThrows
    private Date stringTimeToDate(String time) {
        Date date = new Date();
        String[] splitTime = time.split(":");
        date.setHours(Integer.parseInt(splitTime[0]));
        date.setMinutes(Integer.parseInt(splitTime[1]));
        date.setSeconds(0);
        return date;
    }
}
