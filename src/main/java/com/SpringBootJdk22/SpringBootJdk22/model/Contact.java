package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;
    @Email
    private String email;

    @Column(columnDefinition = "VARCHAR(15) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String phone;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String subject;

    @Lob
    @Column(columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String message;

    // Sử dụng LocalDateTime thay cho Date
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private boolean responded = false;

    @PrePersist
    public void onCreate() {
        // Đặt thời gian hiện tại khi tạo bản ghi
        this.createdDate = LocalDateTime.now();
    }
}
