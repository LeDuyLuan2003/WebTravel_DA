package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Category;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
@Controller
@RequestMapping("/admin/itemCategory")
@RequiredArgsConstructor
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;
    private final CategoryService categoryService;

    // Hiển thị danh sách ItemCategory theo category
    @GetMapping("/category/{categoryId}")
    public String viewItemsByCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        // Lấy category dựa trên ID
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + categoryId));

        // Lấy các ItemCategory thuộc Category
        List<ItemCategory> items = category.getItemCategories();

        // Truyền category và danh sách item vào model
        model.addAttribute("category", category);
        model.addAttribute("itemCategories", items);

        return "admin/itemCategories/itemCategory-list";  // Trả về view để hiển thị ItemCategory theo Category
    }

    @GetMapping("/byCategory/{categoryId}")
    @ResponseBody
    public List<ItemCategory> getItemCategoriesByCategory(@PathVariable("categoryId") Long categoryId) {
        List<ItemCategory> itemCategories = itemCategoryService.findByCategoryId(categoryId);
        return itemCategories;
    }

    // Hiển thị form thêm ItemCategory
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("itemCategory", new ItemCategory()); // Thêm đối tượng itemCategory mới

        // Lấy danh sách Category từ CategoryService và thêm vào model
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("Categories", categories); // Truyền danh sách categories vào model

        return "admin/itemCategories/add-item"; // Đường dẫn đúng tới view
    }


    // Thêm ItemCategory mới
    @PostMapping("/add")
    public String addItemCategory(@Valid @ModelAttribute("itemCategory") ItemCategory itemCategory,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "admin/itemCategories/add-item"; // Không có dấu /
        }
        itemCategoryService.addItemCategory(itemCategory);
        return "redirect:/admin/itemCategory/add";
    }

    // Hiển thị form cập nhật ItemCategory
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        // Lấy thông tin ItemCategory cần cập nhật
        ItemCategory itemCategory = itemCategoryService.getItemCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemCategory Id:" + id));

        // Lấy danh sách Category để hiển thị trong select box
        List<Category> categories = categoryService.getAllCategories();

        // Đưa ItemCategory và danh sách Category vào model
        model.addAttribute("itemCategory", itemCategory);
        model.addAttribute("Categories", categories);

        return "admin/itemCategories/update-item"; // Đường dẫn tới view
    }

    // Cập nhật ItemCategory
    @PostMapping("/update/{id}")
    public String updateItemCategory(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("itemCategory") ItemCategory itemCategory,
                                     BindingResult result) {
        if (result.hasErrors()) {
            itemCategory.setId(id);
            return "admin/itemCategories/update-item";
        }
        itemCategoryService.updateItemCategory(itemCategory);
        return "redirect:/admin/category";
    }

    // Xóa ItemCategory
    @GetMapping("/delete/{id}")
    public String deleteItemCategory(@PathVariable("id") Long id) {
        itemCategoryService.deleteItemCategoryById(id);
        return "redirect:/admin/category";
    }

}
