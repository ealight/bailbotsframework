package com.bailbots.tg.Framework.service;

import com.bailbots.tg.Framework.dao.User;

public interface UserSessionService {
    void addUserToSession(Long telegramId, User user);

    User getUserFromSession(Long telegramId);

}
