package com.SpringBootJdk22.SpringBootJdk22.specification;


import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TourSpecification {


    // Lọc theo khoảng giá (finalPrice)
    public static Specification<Tour> filterByPriceRange(Long priceMin, Long priceMax) {
        return (root, query, criteriaBuilder) -> {
            if (priceMin == null && priceMax == null) return null;
            if (priceMin != null && priceMax != null) {
                return criteriaBuilder.between(root.get("finalPrice"), priceMin, priceMax);
            } else if (priceMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("finalPrice"), priceMin);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("finalPrice"), priceMax);
            }
        };
    }

    // Lọc theo ngày bắt đầu trong TourSchedule
    public static Specification<Tour> filterByStartDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) return null;
            return criteriaBuilder.equal(root.join("schedules").get("startDate"), startDate);
        };
    }

    public static Specification<Tour> filterByCategoryAndItemCategory(String categoryName, String itemCategoryName) {
        return (root, query, criteriaBuilder) -> {
            if (itemCategoryName != null && !itemCategoryName.isEmpty()) {
                // Chỉ lọc theo itemCategory nếu có itemCategory
                var itemCategoryJoin = root.join("itemCategory");
                return criteriaBuilder.equal(itemCategoryJoin.get("name"), itemCategoryName);
            } else if (categoryName != null && !categoryName.isEmpty()) {
                // Chỉ lọc theo category nếu không có itemCategory
                var categoryJoin = root.join("itemCategory").join("category");
                return criteriaBuilder.equal(categoryJoin.get("name"), categoryName);
            }
            return null; // Không có category hoặc itemCategory
        };
    }



}
