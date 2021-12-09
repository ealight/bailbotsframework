package com.bailbots.tg.model;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String authorFirstName;

    @Column
    private String authorLastName;

    @Column
    private String name;

    @Column
    private String time;

    @Column
    private Date date;

    @Column
    private Boolean notification;

    @Override
    public String toString() {
        String date = (new Date().getDay() == this.date.getDay()) ? "Сьогодні" :
                new SimpleDateFormat("dd/MM/yyyy").format(this.date);

        return  "========== " + name +" (" + id + ")" + " ==========\n" +
                "Автор: " + authorFirstName + " " + authorLastName + '\n' +
                "Дата: " + date + "\n" +
                "Час: " + time + "\n\n";
    }
}
