package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.cg.service.ICategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final ICategoryService categoryService;

    public AdminCategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // List all categories
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories"; // moved into templates/admin/
    }

    // Save updates from modal and redirect back to the main list
    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @RequestParam("name") String name,
                         RedirectAttributes ra) {
        try {
            categoryService.update(id, name);
            ra.addFlashAttribute("msg", "Category updated successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", "Update failed: " + ex.getMessage());
        }
        return "redirect:/admin/categories";
    }

    // Create a new category
    @PostMapping
    public String create(@RequestParam("name") String name, RedirectAttributes ra) {
        try {
            categoryService.create(name);
            ra.addFlashAttribute("msg", "Category added successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", "Creation failed: " + ex.getMessage());
        }
        return "redirect:/admin/categories";
    }

    // Delete a category
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            categoryService.delete(id);
            ra.addFlashAttribute("msg", "Category deleted successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", "Delete failed: " + ex.getMessage());
        }
        return "redirect:/admin/categories";
    }
}