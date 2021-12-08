package com.bailbots.tg.service.impl;

import com.bailbots.tg.service.KeyboardItemService;
import com.bailbots.tg.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class KeyboardItemServiceImpl implements KeyboardItemService {
    private final MessageService messageService;

    public KeyboardItemServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void house(Long telegramId) {
        messageService.sendMessage("TT", telegramId);
    }
}
