package com.bailbots.tg.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stickers")
public class Sticker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Override
    public String toString() {
        return "ID: " + id + " | URL: " + url;
    }
}
