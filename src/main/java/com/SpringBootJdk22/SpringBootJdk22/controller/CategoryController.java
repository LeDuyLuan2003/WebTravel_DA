package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Category;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/category")
public class CategoryController {

    private final TourService tourService; // Biến final
    private final CategoryService categoryService; // Biến final

    // Hiển thị danh sách danh mục
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories/category-list"; // Đường dẫn chính xác đến view
    }

    // Hiển thị form thêm Category
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/add-category";
    }

    // Thêm Category mới
    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute("category") Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/categories/add-category";
        }
        categoryService.addCategory(category);
        return "redirect:/admin/category";
    }

    // Hiển thị các sản phẩm theo Category
    @GetMapping("/{id}")
    public String getProductsByCategory(@PathVariable("id") Long categoryId, Model model) {
        List<Tour> tours = tourService.findToursByCategory(categoryId);
        model.addAttribute("products", tours);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/product-list";
    }

    // Hiển thị form cập nhật Category
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/categories/update-category";
    }

    // Cập nhật Category
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid @ModelAttribute("category") Category category,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            category.setId(id);
            return "admin/categories/update-category";
        }
        categoryService.updateCategory(category);
        return "redirect:/admin/category";
    }

    // Xóa Category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/category";
    }


}