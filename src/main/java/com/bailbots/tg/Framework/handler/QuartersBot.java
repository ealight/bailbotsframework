package com.bailbots.tg.Framework.handler;

import com.bailbots.tg.Framework.bpp.BotSelectHandle;
import com.bailbots.tg.Framework.config.BotConfiguration;
import com.bailbots.tg.Framework.dao.User;
import com.bailbots.tg.Framework.dto.RegistrationDto;
import com.bailbots.tg.Framework.service.UserService;
import com.bailbots.tg.Framework.service.impl.UserSessionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class QuartersBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(QuartersBot.class);

    private final UserService userService;
    private final UserSessionServiceImpl userSessionService;

    public QuartersBot(@Lazy UserService userService, UserSessionServiceImpl userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotSelectHandle.processByUpdate(update);

        Message message = extractMessageFromUpdate(update);

        Long telegramId = message.getChatId();
        Chat chat = message.getChat();

        boolean userExistInSession = userSessionService.getUserFromSession(telegramId) != null;

        if(!userExistInSession) {
            if(!userService.isUserExist(telegramId)) {
                String firstName = chat.getFirstName();
                String lastName = chat.getLastName();

                RegistrationDto registrationDto = createRegistrationDto(telegramId, firstName, lastName);

                User user = userService.registerUser(registrationDto);

                userSessionService.addUserToSession(telegramId, user);
            }
            else {
                LOGGER.info("Telegram ID #{} successfully logged!", telegramId);
                userSessionService.addUserToSession(telegramId, new User());
            }

        }

    }

    @Override
    public String getBotUsername() {
        return BotConfiguration.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfiguration.BOT_TOKEN;
    }

    private RegistrationDto createRegistrationDto(Long telegramId, String firstName, String lastName) {
        return RegistrationDto.builder()
                .telegramId(telegramId)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private Message extractMessageFromUpdate(Update update) {
        Message message = update.getMessage();

        if (message == null) {
            if (update.getCallbackQuery().getMessage() != null) {
                message = update.getCallbackQuery().getMessage();
            }
            else {
                return null;
            }
        }
        return message;
    }
}
