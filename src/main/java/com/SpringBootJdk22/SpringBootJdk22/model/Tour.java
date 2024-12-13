package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
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
    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;

    private long price;

    @Lob
    @Column(columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String description;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "itemCategory_id")
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    private long discountPercentage;
    private long finalPrice;
    @PrePersist
    @PreUpdate
    public void calculateFinalPrice() {
        if (discountPercentage > 0) {
            finalPrice = price - (price * discountPercentage / 100);
        } else {
            finalPrice = price;
        }
        finalPrice = Math.max(finalPrice, 0);
    }
}

