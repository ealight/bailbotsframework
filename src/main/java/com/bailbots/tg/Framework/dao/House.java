package com.bailbots.tg.Framework.dao;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "detail_info")
    private String detailInfo;

    @Column(name = "owner_requirements")
    private String ownerRequirements;

    @Column(name = "owner_phone_number")
    private String ownerPhoneNumber;

    @Column(name = "additional_service")
    private String additionalService;

    @ToString.Exclude
    @OneToMany(mappedBy = "house", fetch = FetchType.EAGER)
    private List<HouseImageData> images;
}
