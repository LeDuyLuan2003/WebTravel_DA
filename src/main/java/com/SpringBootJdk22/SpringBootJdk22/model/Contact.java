package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String subject;

    @Lob
    private String message;

    private LocalDateTime createdDate ;

    private boolean responded = false;


    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDateTime.now().withSecond(0).withNano(0); // Loại bỏ giây và mili-giây
    }
}
