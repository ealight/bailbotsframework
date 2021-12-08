package com.bailbots.tg.service;

import com.bailbots.tg.model.Event;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;

public interface EventsService {
    void addEvent(Update update);
    List<Event> getAllEventsToday();
    List<Event> getAllEvents();
    void notificationWeekend();
    void setWeekendTime(String time);
    String getWeekendTime();
}
