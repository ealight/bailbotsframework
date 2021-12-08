package com.bailbots.tg.repository;

import com.bailbots.tg.model.Event;
import com.bailbots.tg.model.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StickerRepository extends JpaRepository<Sticker, Long> {
    @Query(nativeQuery=true, value="SELECT *  FROM stickers ORDER BY random() LIMIT 1")
    Sticker getRandomSticker();
}
