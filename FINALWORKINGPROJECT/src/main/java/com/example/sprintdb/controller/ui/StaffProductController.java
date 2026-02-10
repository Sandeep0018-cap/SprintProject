package com.example.sprintdb.controller.ui;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.repository.VendorRepository;
import com.example.sprintdb.service.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/staff/products")
public class StaffProductController {

    private final ProductService productService;
    private final VendorRepository vendorRepository;

    public StaffProductController(ProductService productService,
                                  VendorRepository vendorRepository) {
        this.productService = productService;
        this.vendorRepository = vendorRepository;
    }

    // GET /staff/products/new?categoryId=123
    @GetMapping("/new")
    public String newProduct(@RequestParam("categoryId") Long categoryId, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new ProductDto());
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("vendors", vendorRepository.findAll());  // ✅ provide vendors
        model.addAttribute("postAction", "/staff/products");
        return "product-form";
    }

    // POST /staff/products
    @PostMapping
    public String create(@Valid @ModelAttribute("product") ProductDto product,
                         BindingResult binding,
                         @RequestParam("categoryId") Long categoryId,
                         RedirectAttributes ra,
                         Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("postAction", "/staff/products");
        model.addAttribute("vendors", vendorRepository.findAll());

        // Basic validation: vendor required by DB
        if (product.getVendorId() == null) {
            binding.rejectValue("vendorId", "NotNull", "Vendor is required");
        }

        if (binding.hasErrors()) {
            return "product-form";
        }

        try {
            productService.createOrUpdate(product, categoryId);
        } catch (DataIntegrityViolationException ex) {
            // Map only duplicate SKU to the sku field; show generic for other constraints
            String msg = getRootMessage(ex).toLowerCase();
            if (msg.contains("duplicate") || msg.contains("uk_") || msg.contains("unique")) {
                binding.rejectValue("skuId", "sku.duplicate", "SKU already exists");
            } else {
                model.addAttribute("error", "Could not save product: " + getRootMessage(ex));
            }
            return "product-form";
        }

        ra.addFlashAttribute("success", "Product added successfully");
        return "redirect:/categories/" + categoryId;
    }

    // POST /staff/products/{id}/delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam("categoryId") Long categoryId,
                         RedirectAttributes ra) {
        try {
            productService.delete(id);
            ra.addFlashAttribute("success", "Product deleted");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error",
                "Cannot delete this product because it has sales/purchase records. You can archive it instead.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Failed to delete product: " + getRootMessage(ex));
        }
        return "redirect:/categories/" + categoryId;
    }

    private String getRootMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null) c = c.getCause();
        return c.getMessage() != null ? c.getMessage() : t.getMessage();
    }
}