package com.example.sprintdb.controller.ui;

import com.example.sprintdb.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping
<<<<<<< HEAD
    public String create(@RequestParam String name, RedirectAttributes ra) {
        try {
            categoryService.create(name);
            ra.addFlashAttribute("msg", "Category added successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/categories";
    }

=======
    public String create(@RequestParam String name) {
        categoryService.create(name);
        return "redirect:/admin/categories";
    }

//    @PostMapping("/{id}/delete")
//    public String delete(@PathVariable Long id) {
//        categoryService.delete(id);
//        return "redirect:/admin/categories";
//    }
>>>>>>> 4dae07659c187a62e65e7087a60de0d7f1af2310
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            categoryService.delete(id);
            ra.addFlashAttribute("msg", "Category deleted");
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            ra.addFlashAttribute("err", "Cannot delete: category is in use by one or more products.");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", "Delete failed: " + ex.getMessage());
        }
        return "redirect:/admin/categories";
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 4dae07659c187a62e65e7087a60de0d7f1af2310
