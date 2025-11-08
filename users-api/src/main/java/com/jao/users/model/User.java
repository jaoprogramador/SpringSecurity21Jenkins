package com.jao.users.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data // Proporciona getters, setters, toString, equals y hashCode (Lombok)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {@Index(columnList = "email", unique = true)})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false, unique=true)
    private String email;
    private Instant createdAt = Instant.now();

    
}

