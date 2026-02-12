package com.cg.controller;

import com.cg.dto.ProductDto;
import com.cg.repository.CategoryRepository;
import com.cg.repository.VendorRepository;
import com.cg.service.ProductService;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
// import org.springframework.security.access.prepost.PreAuthorize; // uncomment if you enforce STAFF
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

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public ProductController(ProductService productService,
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

    /** ---------- CREATE ---------- */

    /** GET /products/new?categoryId=123 */
//  @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/new", produces = MediaType.TEXT_HTML_VALUE)
    public String newProduct(@RequestParam("categoryId") Long categoryId, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new ProductDto());
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("vendors", vendorRepository.findAll());
        return "product-form";
    }

    /** POST /products */
//  @PreAuthorize("hasRole('STAFF')")
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
            return "product-form";
        }
        if (product.getVendorId() == null) {
            binding.rejectValue("vendorId", "NotNull", "Vendor is required");
        }
        if (binding.hasErrors()) {
            return "product-form";
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
            return "product-form";
        }

        ra.addFlashAttribute("success", "Product added successfully");
        return "redirect:/categories/" + categoryId;
    }

    /** ---------- EDIT ---------- */

    /** GET /products/{id}/edit?categoryId=123 */
//  @PreAuthorize("hasRole('STAFF')")
    @GetMapping(path = "/{id}/edit", produces = MediaType.TEXT_HTML_VALUE)
    public String edit(@PathVariable Long id,
                       @RequestParam(value = "categoryId", required = false) Long categoryId,
                       Model model,
                       RedirectAttributes ra) {

        try {
            ProductDto dto = productService.getByIdAsDto(id); // must exist in service
            Long resolvedCategoryId = (categoryId != null) ? categoryId : dto.getCategoryId();

            model.addAttribute("product", dto);
            model.addAttribute("categoryId", resolvedCategoryId);
            model.addAttribute("vendors", vendorRepository.findAll());
            return "product-form";
        } catch (RuntimeException ex) {
            // ResourceNotFoundException, etc.
            ra.addFlashAttribute("error", "Product not found or cannot be loaded.");
            Long fallback = (categoryId != null) ? categoryId : 0L;
            return "redirect:/categories/" + fallback;
        }
    }

    /** PUT /products/{id} */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("product") ProductDto product,
                         BindingResult binding,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         RedirectAttributes ra,
                         Model model) {

        // Ensure the ID from the URL is set in the DTO
        product.setProductId(id);
        
        Long resolvedCategoryId = (categoryId != null) ? categoryId : product.getCategoryId();

        model.addAttribute("categoryId", resolvedCategoryId);
        model.addAttribute("vendors", vendorRepository.findAll());

        if (resolvedCategoryId == null) {
            binding.reject("category.required", "Missing category. Please try again from the category page.");
        }
        
        if (binding.hasErrors()) {
            return "product-form";
        }

        try {
            productService.createOrUpdate(product, resolvedCategoryId);
        } catch (DataIntegrityViolationException ex) {
            String msg = getRootMessage(ex).toLowerCase();
            if (msg.contains("duplicate") || msg.contains("uk_") || msg.contains("unique")) {
                binding.rejectValue("skuId", "sku.duplicate", "SKU already exists");
            } else {
                model.addAttribute("error", "Could not update product: " + getRootMessage(ex));
            }
            return "product-form";
        }

        ra.addFlashAttribute("success", "Product updated successfully");
        return "redirect:/categories/" + resolvedCategoryId;
    }

    /** ---------- DELETE ---------- */

    /** DELETE /products/{id}?categoryId=123 */
//  @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping(path = "/{id}")
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