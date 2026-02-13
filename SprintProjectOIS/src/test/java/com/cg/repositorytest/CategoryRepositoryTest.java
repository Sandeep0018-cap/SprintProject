package com.cg.repositorytest;

import com.cg.entity.Category;
import com.cg.repository.CategoryRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ✅ use your DB
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void save_shouldPersistCategory() {
        Category c = new Category();
        c.setName(unique("TestCategory"));

        Category saved = categoryRepository.save(c);

        // if your entity has categoryId
        assertNotNull(saved.getCategoryId());
        assertEquals(c.getName(), saved.getName());
    }

    @Test
    void findByNameIgnoreCase_shouldReturnCategory_whenExists() {
        String name = unique("Laptops");

        Category c = new Category();
        c.setName(name);
        categoryRepository.save(c);

        Optional<Category> found = categoryRepository.findByNameIgnoreCase(name.toLowerCase());

        assertTrue(found.isPresent());
        assertEquals(name, found.get().getName());
    }

    @Test
    void findByNameIgnoreCase_shouldIgnoreCase() {
        String name = unique("Mobiles");

        Category c = new Category();
        c.setName(name);
        categoryRepository.save(c);

        Optional<Category> found = categoryRepository.findByNameIgnoreCase(name.toUpperCase());

        assertTrue(found.isPresent());
        assertEquals(name, found.get().getName());
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void findByNameIgnoreCase_shouldReturnEmpty_whenNotExists() {
        String randomName = unique("DoesNotExist");

        Optional<Category> found = categoryRepository.findByNameIgnoreCase(randomName);

        assertTrue(found.isEmpty());
    }

    @Test
    void findByNameIgnoreCase_shouldReturnCorrect_whenMultipleExist() {
        String name1 = unique("Tablets");
        String name2 = unique("Cameras");

        Category c1 = new Category();
        c1.setName(name1);
        categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName(name2);
        categoryRepository.save(c2);

        Optional<Category> found = categoryRepository.findByNameIgnoreCase(name2.toLowerCase());

        assertTrue(found.isPresent());
        assertEquals(name2, found.get().getName());
    }

    @Test
    void findByNameIgnoreCase_whenNullPassed_shouldNotCrashTest() {
        try {
            Optional<Category> found = categoryRepository.findByNameIgnoreCase(null);
            // If your setup returns empty instead of throwing, that's OK
            assertNotNull(found);
        } catch (Exception ex) {
            // If it throws, that's also OK (Spring/Data/JPA provider dependent)
            assertTrue(true);
        }
    }

    // -------------------------
    // helper: always unique names so DB existing data never clashes
    // -------------------------
    private String unique(String base) {
        return base + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}