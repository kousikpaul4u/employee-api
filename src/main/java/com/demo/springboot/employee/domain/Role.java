package com.demo.springboot.employee.domain;

import javax.persistence.*;

import lombok.*;


@Entity
@Builder
@Table(name="app_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
