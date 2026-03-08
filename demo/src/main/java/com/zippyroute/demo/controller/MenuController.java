package com.zippyroute.demo.controller;

import com.zippyroute.demo.dto.MenuItemDTO;
import com.zippyroute.demo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/all")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        List<MenuItemDTO> items = menuService.getAllMenuItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItemDTO>> getAvailableMenuItems() {
        List<MenuItemDTO> items = menuService.getAvailableMenuItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemDTO> items = menuService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = menuService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        MenuItemDTO item = menuService.getMenuItemById(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<MenuItemDTO>> getTopRatedItems() {
        List<MenuItemDTO> items = menuService.getTopRatedItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/create")
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO dto) {
        MenuItemDTO created = menuService.createMenuItem(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO dto) {
        MenuItemDTO updated = menuService.updateMenuItem(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}
