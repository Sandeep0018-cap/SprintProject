package com.cg.controller;

import com.cg.dto.ProductDto;
import com.cg.repository.CategoryRepository;
import com.cg.repository.VendorRepository;
import com.cg.service.IProductService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public ProductController(IProductService productService,
                             CategoryRepository categoryRepository,
                             VendorRepository vendorRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping(path = "/new", produces = MediaType.TEXT_HTML_VALUE)
    public String newProduct(@RequestParam("categoryId") Long categoryId, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new ProductDto());
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("vendors", vendorRepository.findAll());
        return "products/product-form"; // moved into templates/products/
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String create(@Valid @ModelAttribute("product") ProductDto product,
                         BindingResult binding,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         RedirectAttributes ra,
                         Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("vendors", vendorRepository.findAll());
        if (categoryId == null) {
            model.addAttribute("error", "Missing category. Please start from the category page and try again.");
            return "products/product-form";
        }
        if (product.getVendorId() == null) {
            binding.rejectValue("vendorId", "NotNull", "Vendor is required");
        }
        if (binding.hasErrors()) {
            return "products/product-form";
        }
        try {
            productService.createOrUpdate(product, categoryId);
        } catch (DataIntegrityViolationException ex) {
            String msg = getRootMessage(ex).toLowerCase();
            if (msg.contains("duplicate") || msg.contains("uk_") || msg.contains("unique")) {
                binding.rejectValue("skuId", "sku.duplicate", "SKU already exists");
            } else {
                model.addAttribute("error", "Could not save product: " + getRootMessage(ex));
            }
            return "products/product-form";
        }
        ra.addFlashAttribute("success", "Product added successfully");
        return "redirect:/categories/" + categoryId;
    }

    @GetMapping(path = "/{id}/edit", produces = MediaType.TEXT_HTML_VALUE)
    public String edit(@PathVariable Long id,
                       @RequestParam(value = "categoryId", required = false) Long categoryId,
                       Model model,
                       RedirectAttributes ra) {
        try {
            ProductDto dto = productService.getByIdAsDto(id);
            Long resolvedCategoryId = (categoryId != null) ? categoryId : dto.getCategoryId();
            model.addAttribute("product", dto);
            model.addAttribute("categoryId", resolvedCategoryId);
            model.addAttribute("vendors", vendorRepository.findAll());
            return "products/product-form";
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", "Product not found or cannot be loaded.");
            return "redirect:/categories/" + (categoryId != null ? categoryId : 0L);
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("product") ProductDto product,
                         BindingResult binding,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         RedirectAttributes ra,
                         Model model) {
        product.setProductId(id);
        Long resolvedCategoryId = (categoryId != null) ? categoryId : product.getCategoryId();
        model.addAttribute("categoryId", resolvedCategoryId);
        model.addAttribute("vendors", vendorRepository.findAll());
        if (resolvedCategoryId == null) {
            binding.reject("category.required", "Missing category context.");
        }
        if (binding.hasErrors()) {
            return "products/product-form";
        }
        try {
            productService.createOrUpdate(product, resolvedCategoryId);
        } catch (DataIntegrityViolationException ex) {
            handleDuplicateSku(ex, binding, model);
            return "products/product-form";
        }
        ra.addFlashAttribute("success", "Product updated successfully");
        return "redirect:/categories/" + resolvedCategoryId;
    }

    @DeleteMapping(path = "/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam("categoryId") Long categoryId,
                         RedirectAttributes ra) {
        try {
            productService.delete(id);
            ra.addFlashAttribute("success", "Product deleted");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error", "Cannot delete product with existing sales/purchase history.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Deletion failed: " + getRootMessage(ex));
        }
        return "redirect:/categories/" + categoryId;
    }

    private String getRootMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null) c = c.getCause();
        return c.getMessage() != null ? c.getMessage() : t.getMessage();
    }

    private void handleDuplicateSku(org.springframework.dao.DataIntegrityViolationException ex,
                                    BindingResult binding, Model model) {
        String msg = getRootMessage(ex).toLowerCase();
        if (msg.contains("duplicate") || msg.contains("uk_") || msg.contains("unique")) {
            binding.rejectValue("skuId", "sku.duplicate", "SKU already exists");
        } else {
            model.addAttribute("error", "Update failed: " + getRootMessage(ex));
        }
    }
}
