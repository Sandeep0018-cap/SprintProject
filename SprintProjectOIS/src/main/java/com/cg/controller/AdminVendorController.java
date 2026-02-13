package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.cg.service.IVendorService;

@Controller
@RequestMapping("/admin/vendors")
public class AdminVendorController {

    private final IVendorService vendorService;

    public AdminVendorController(IVendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vendors", vendorService.findAll());
        return "admin/vendors"; // moved into templates/admin/
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) String contactName,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String email,
                         RedirectAttributes ra) {
        try {
            vendorService.update(id, name, contactName, phone, email);
            ra.addFlashAttribute("msg", "Vendor updated successfully");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/vendors";
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

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            vendorService.delete(id);
            ra.addFlashAttribute("msg", "Vendor deleted");
        } catch (Exception ex) {
            ra.addFlashAttribute("err", ex.getMessage());
        }
        return "redirect:/admin/vendors";
    }
}