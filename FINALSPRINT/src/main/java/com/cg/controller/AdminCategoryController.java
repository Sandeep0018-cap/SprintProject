package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin-categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "admin-category-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         RedirectAttributes ra) {
        try {
            categoryService.update(id, name);
            ra.addFlashAttribute("msg", "Category updated successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping
    public String create(@RequestParam String name, RedirectAttributes ra) {
        try {
            categoryService.create(name);
            ra.addFlashAttribute("msg", "Category added successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            categoryService.delete(id);
            ra.addFlashAttribute("msg", "Category deleted");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/categories";
    }
}