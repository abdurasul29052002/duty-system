package com.example.dutysystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Duty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private boolean active=true;

    @ManyToOne
    private User currentDuty;

    @ManyToMany
    private Set<User> users;
}
