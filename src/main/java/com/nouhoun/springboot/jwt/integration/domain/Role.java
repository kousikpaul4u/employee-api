package com.nouhoun.springboot.jwt.integration.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="app_role")
@Getter
@Setter
public class Role {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name")
    private String roleName;

    @Column(name="description")
    private String description;
}
