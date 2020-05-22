package com.bailbots.tg.Framework.service;

import com.bailbots.tg.Framework.dao.User;
import com.bailbots.tg.Framework.dto.RegistrationDto;


public interface UserService {
    User registerUser(RegistrationDto registrationDto);

    boolean isUserExist(Long telegramId);

}
