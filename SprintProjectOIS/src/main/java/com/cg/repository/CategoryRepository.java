package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.Category;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> { // Provides standard CRUD operations for Category entities

    // Executes a query to find a category by name while ignoring character casing
    Optional<Category> findByNameIgnoreCase(String name); 

}
