package com.bailbots.tg.Framework.service.impl;

import com.bailbots.tg.Framework.dao.User;
import com.bailbots.tg.Framework.dto.RegistrationDto;
import com.bailbots.tg.Framework.repository.UserRepository;
import com.bailbots.tg.Framework.service.MessageService;
import com.bailbots.tg.Framework.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final MessageService messageService;

    public UserServiceImpl(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Override
    public User registerUser(RegistrationDto dto) {
        User user = User.builder()
                .telegramId(dto.getTelegramId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();

        messageService.sendMessage("Successfully register!", dto.getTelegramId());

        LOGGER.info("Telegram ID #{} successfully register!",  dto.getTelegramId());

        return userRepository.save(user);
    }

    @Override
    public boolean isUserExist(Long telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }

}
