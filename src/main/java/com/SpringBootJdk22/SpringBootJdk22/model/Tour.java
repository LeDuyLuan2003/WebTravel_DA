package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NumberFormat(pattern = "#,###")
    private double price;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private String avatar;

    @ManyToOne
    @JoinColumn(name = "itemCategory_id")
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    private double discountPercentage; // Phần trăm giảm giá
    @NumberFormat(pattern = "#,###")
    private double finalPrice; // Giá sau khi giảm (lưu trực tiếp vào DB)

    // Tính giá sau giảm giá
    @PrePersist
    @PreUpdate
    public void calculateFinalPrice() {
        if (discountPercentage > 0) {
            finalPrice = price - (price * discountPercentage / 100);
        } else {
            finalPrice = price; // Nếu không có giảm giá, giữ nguyên giá gốc
        }
        finalPrice = Math.max(finalPrice, 0); // Đảm bảo giá không âm
    }
}
