package com.bailbots.tg.Framework.repository;

import com.bailbots.tg.Framework.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByTelegramId(Long telegramId);

}
