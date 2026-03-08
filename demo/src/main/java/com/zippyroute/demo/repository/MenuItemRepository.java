package com.zippyroute.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zippyroute.demo.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByAvailableTrue();
    List<MenuItem> findByAvailableTrueOrderByRatingDesc();
    
    @Query("SELECT DISTINCT m.category FROM MenuItem m")
    List<String> findAllCategories();
}
