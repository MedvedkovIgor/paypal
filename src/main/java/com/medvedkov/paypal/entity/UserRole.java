package com.medvedkov.paypal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_ROLES")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    private String name;

    UserRole() {
    }

    public UserRole(String name) {
        this.name = name;
    }

}