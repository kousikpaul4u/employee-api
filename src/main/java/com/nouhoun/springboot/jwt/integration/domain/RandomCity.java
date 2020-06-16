package com.nouhoun.springboot.jwt.integration.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "random_city")
@Getter
@Setter
public class RandomCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
