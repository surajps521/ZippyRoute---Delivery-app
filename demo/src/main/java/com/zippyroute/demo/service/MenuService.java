package com.zippyroute.demo.service;

import com.zippyroute.demo.dto.MenuItemDTO;
import com.zippyroute.demo.entity.MenuItem;
import com.zippyroute.demo.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItemDTO createMenuItem(MenuItemDTO dto) {
        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCategory(dto.getCategory());
        item.setImageUrl(dto.getImageUrl());
        item.setAvailable(true);

        MenuItem savedItem = menuItemRepository.save(item);
        return convertToDTO(savedItem);
    }

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return menuItemRepository.findAllCategories();
    }

    public MenuItemDTO getMenuItemById(Long id) {
        MenuItem item = menuItemRepository.findById(id).orElse(null);
        return item != null ? convertToDTO(item) : null;
    }

    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO dto) {
        MenuItem item = menuItemRepository.findById(id).orElse(null);
        if (item != null) {
            item.setName(dto.getName());
            item.setDescription(dto.getDescription());
            item.setPrice(dto.getPrice());
            item.setCategory(dto.getCategory());
            item.setImageUrl(dto.getImageUrl());
            item.setAvailable(dto.getAvailable());
            item.setUpdatedAt(System.currentTimeMillis());

            MenuItem updated = menuItemRepository.save(item);
            return convertToDTO(updated);
        }
        return null;
    }

    public List<MenuItemDTO> getTopRatedItems() {
        return menuItemRepository.findByAvailableTrueOrderByRatingDesc().stream()
                .limit(10)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MenuItemDTO convertToDTO(MenuItem item) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setCategory(item.getCategory());
        dto.setImageUrl(item.getImageUrl());
        dto.setRating(item.getRating());
        dto.setReviewCount(item.getReviewCount());
        dto.setAvailable(item.getAvailable());
        return dto;
    }
}
