package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;

    public List<ItemCategory> getAllItemCategories() {
        return itemCategoryRepository.findAll();
    }

    public Optional<ItemCategory> getItemCategoryById(Long id) {
        return itemCategoryRepository.findById(id);
    }

    public void addItemCategory(ItemCategory itemCategory) {
        itemCategoryRepository.save(itemCategory);
    }

    public void updateItemCategory(@NotNull ItemCategory itemCategory) {
        ItemCategory existingItemCategory = itemCategoryRepository.findById(itemCategory.getId())
                .orElseThrow(() -> new IllegalStateException("Item Category with ID " + itemCategory.getId() + " does not exist."));
        existingItemCategory.setName(itemCategory.getName());
        itemCategoryRepository.save(existingItemCategory);
    }

    public void deleteItemCategoryById(Long id) {
        if (!itemCategoryRepository.existsById(id)) {
            throw new IllegalStateException("Item Category with ID " + id + " does not exist.");
        }
        itemCategoryRepository.deleteById(id);
    }

    // Thêm phương thức findByCategoryId
    public List<ItemCategory> findByCategoryId(Long categoryId) {
        return itemCategoryRepository.findByCategoryId(categoryId);
    }
}
