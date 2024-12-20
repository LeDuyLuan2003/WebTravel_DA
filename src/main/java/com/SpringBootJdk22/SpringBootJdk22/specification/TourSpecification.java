package com.SpringBootJdk22.SpringBootJdk22.specification;


import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import org.springframework.data.jpa.domain.Specification;

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
    public static Specification<Tour> filterByStartDate(String startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || startDate.isEmpty()) return null;
            return criteriaBuilder.equal(root.join("schedules").get("startDate"), startDate);
        };
    }

    // Lọc theo tên Category
    public static Specification<Tour> filterByCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) return null;
            return criteriaBuilder.equal(root.join("itemCategory").join("category").get("name"), categoryName);
        };
    }

    // Lọc theo tên ItemCategory
    public static Specification<Tour> filterByItemCategory(String itemCategoryName) {
        return (root, query, criteriaBuilder) -> {
            if (itemCategoryName == null || itemCategoryName.isEmpty()) return null;
            return criteriaBuilder.equal(root.join("itemCategory").get("name"), itemCategoryName);
        };
    }
}
