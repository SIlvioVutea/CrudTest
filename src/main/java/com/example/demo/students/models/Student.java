package com.example.demo.students.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private Boolean isWorking;
}
