package com.medvedkov.paypal.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DEFAULT_ENTITY")
@Data
public class DefaultEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
