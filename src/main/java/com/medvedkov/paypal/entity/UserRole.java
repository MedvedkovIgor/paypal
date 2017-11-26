package com.medvedkov.paypal.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLES")
@NoArgsConstructor
public class UserRole extends DefaultEntity {
    @Getter
    @Setter
    private String name;

    public UserRole(String name) {
        this.name = name;
    }
}