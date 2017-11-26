package com.medvedkov.paypal.entity;

import javax.persistence.*;

@Entity
@Table(name = "DEFAULT_ENTITY")
public class DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
