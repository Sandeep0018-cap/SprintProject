package com.example.sprintdb.controller.ui;

import com.example.sprintdb.service.VendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/vendors")
public class AdminVendorController {

    private final VendorService vendorService;

    public AdminVendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vendors", vendorService.findAll());
        return "admin-vendors";
    }

    @PostMapping
    public String create(@RequestParam String name,
                        @RequestParam(required = false) String contactName,
                        @RequestParam(required = false) String phone,
                        @RequestParam(required = false) String email,
                        RedirectAttributes ra) {
        try {
            vendorService.create(name, contactName, phone, email);
            ra.addFlashAttribute("msg", "Vendor added successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/vendors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            vendorService.delete(id);
            ra.addFlashAttribute("msg", "Vendor deleted");
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            ra.addFlashAttribute("err", "Cannot delete: vendor is linked to products or restock requests.");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", "Delete failed: " + ex.getMessage());
        }
        return "redirect:/admin/vendors";
    }
}