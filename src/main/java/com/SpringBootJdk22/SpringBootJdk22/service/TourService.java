package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.Image;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.repository.ImageRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;

    // Retrieve all products from the database
    public List<Tour> getAllProducts() {
        return tourRepository.findAll();
    }

    // Retrieve a product by its id
    public Optional<Tour> getProductById(Long id) {
        return tourRepository.findById(id);
    }

    public List<Tour> findProductsByName(String name) {
        return tourRepository.findByNameContainingIgnoreCase(name);
    }

    // Find tours by item category
    public List<Tour> findByItemCategory(ItemCategory itemCategory) {
        return tourRepository.findByItemCategory(itemCategory);
    }
    // Add a new product to the database
    public Tour addProduct(Tour tour) {
        return tourRepository.save(tour);
    }

    // Update an existing product
    public Tour updateProduct(@NotNull Tour tour) {
        Tour existingTour = tourRepository.findById(tour.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        tour.getId() + " does not exist."));

        // Update fields
        existingTour.setName(tour.getName());
        existingTour.setPrice(tour.getPrice());
        existingTour.setDiscountPercentage(tour.getDiscountPercentage());
        existingTour.setDescription(tour.getDescription());
        existingTour.setItemCategory(tour.getItemCategory());
        existingTour.setAvatar(tour.getAvatar());

        return tourRepository.save(existingTour);
    }

    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        tourRepository.deleteById(id);
    }

    // Add a new image
    public Image addImage(Image image) {
        return imageRepository.save(image);
    }

    // Delete an image by its id
    public void deleteImageById(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new IllegalStateException("Image with ID " + id + " does not exist.");
        }
        imageRepository.deleteById(id);
    }

    public List<Tour> findProductsByCategory(Long categoryId) {
        return tourRepository.findByItemCategory_Id(categoryId);
    }
}
