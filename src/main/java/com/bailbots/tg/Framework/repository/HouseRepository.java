package com.bailbots.tg.Framework.repository;

import com.bailbots.tg.Framework.dao.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    House getById(Long id);

}
