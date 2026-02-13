package com.cg.controller;

import com.cg.dto.RestockDto;
import com.cg.entity.Product;
import com.cg.entity.RestockRequest;
import com.cg.entity.Vendor;
import com.cg.enums.RestockStatus;
import com.cg.service.IRestockService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/staff/restock")
public class RestockController {

    private final IRestockService restockService;

    public RestockController(IRestockService restockService) {
        this.restockService = restockService;
    }

    @GetMapping("/new")
    public String form(@RequestParam(value = "vendorId", required = false) Long vendorId, Model model) {
        RestockDto req = (RestockDto) model.getAttribute("req");
        if (req == null) {
            req = new RestockDto();
            req.setVendorId(vendorId);
            req.setRequestedQty(1);
        }
        populateRestockModel(model, req, vendorId, false);
        return "staff/restock-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("req") RestockDto req,
                         BindingResult binding,
                         Authentication auth,
                         Model model,
                         RedirectAttributes ra) {

        validate(req, binding);
        if (binding.hasErrors()) {
            populateRestockModel(model, req, req.getVendorId(), false);
            return "staff/restock-form";
        }

        // Resolve entities via service
        Product product = restockService.getProduct(req.getProductId());
        Vendor vendor = restockService.getVendor(req.getVendorId());

        if (product == null) {
            binding.rejectValue("productId", "NotFound", "Product not found");
        }
        if (vendor == null) {
            binding.rejectValue("vendorId", "NotFound", "Vendor not found");
        }
        if (binding.hasErrors()) {
            populateRestockModel(model, req, req.getVendorId(), false);
            return "staff/restock-form";
        }

        // Create
        restockService.createRestockRequest(req, product, vendor, auth);
        ra.addFlashAttribute("msg", "Restock request submitted!");
        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        RestockRequest rr = restockService.getRestockById(id);
        RestockDto form = new RestockDto();
        form.setId(rr.getId());
        form.setProductId(rr.getProduct().getProductId());
        form.setVendorId(rr.getVendor().getVendorId());
        form.setRequestedQty(rr.getRequestedQty());
        populateRestockModel(model, form, form.getVendorId(), true);
        return "staff/restock-form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("req") RestockDto req,
                         BindingResult binding,
                         RedirectAttributes ra,
                         Model model) {

        RestockRequest rr = restockService.getRestockById(id);
        validate(req, binding);
        if (binding.hasErrors()) {
            populateRestockModel(model, req, req.getVendorId(), true);
            return "staff/restock-form";
        }

        Product product = restockService.getProduct(req.getProductId());
        Vendor vendor = restockService.getVendor(req.getVendorId());

        if (product == null) {
            binding.rejectValue("productId", "NotFound", "Product not found");
        }
        if (vendor == null) {
            binding.rejectValue("vendorId", "NotFound", "Vendor not found");
        }
        if (binding.hasErrors()) {
            populateRestockModel(model, req, req.getVendorId(), true);
            return "staff/restock-form";
        }

        restockService.updateRestockRequest(req, rr, product, vendor);
        ra.addFlashAttribute("msg", "Restock request updated!");
        return "redirect:/dashboard";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        RestockRequest rr = restockService.getRestockById(id);
        if (rr.getStatus() != RestockStatus.PENDING) {
            ra.addFlashAttribute("error", "Only PENDING requests can be deleted.");
            return "redirect:/dashboard";
        }
        restockService.deleteRestockRequest(rr);
        ra.addFlashAttribute("msg", "Restock request deleted!");
        return "redirect:/dashboard";
    }

    private void populateRestockModel(Model model, RestockDto req, Long vendorId, boolean isEdit) {
        model.addAttribute("vendors", restockService.getAllVendors());
        model.addAttribute("selectedVendorId", vendorId);
        model.addAttribute("products", vendorId != null
                ? restockService.getProductsByVendor(vendorId)
                : Collections.emptyList());
        model.addAttribute("req", req);
        model.addAttribute("isEdit", isEdit);
    }

    /**
     * Performs request-level validation and uses service for cross-entity checks.
     */
    private void validate(RestockDto req, BindingResult binding) {
        if (req.getVendorId() == null) {
            binding.rejectValue("vendorId", "NotNull", "Vendor required");
        }
        if (req.getProductId() == null) {
            binding.rejectValue("productId", "NotNull", "Product required");
        }
        // Check relation only if basic constraints passed
        if (!binding.hasErrors() && req.getVendorId() != null && req.getProductId() != null) {
            boolean linked = restockService.productBelongsToVendor(req.getVendorId(), req.getProductId());
            if (!linked) {
                binding.rejectValue("productId", "Mismatch", "Product not supplied by this vendor");
            }
        }
    }
}