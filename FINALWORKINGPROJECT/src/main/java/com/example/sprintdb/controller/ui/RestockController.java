package com.example.sprintdb.controller.ui;

import com.example.sprintdb.entity.Product;
import com.example.sprintdb.entity.RestockRequest;
import com.example.sprintdb.entity.RestockStatus;
import com.example.sprintdb.entity.Vendor;
import com.example.sprintdb.repository.ProductRepository;
import com.example.sprintdb.repository.RestockRequestRepository;
import com.example.sprintdb.repository.VendorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/staff/restock")
public class RestockController {

    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final RestockRequestRepository restockRequestRepository;

    public RestockController(VendorRepository vendorRepository,
                             ProductRepository productRepository,
                             RestockRequestRepository restockRequestRepository) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.restockRequestRepository = restockRequestRepository;
    }

    /* ===========================
       CREATE (GET with vendor filter)
       =========================== */
    @GetMapping("/new")
    public String form(@RequestParam(value = "vendorId", required = false) Long vendorId,
                       Model model) {

        model.addAttribute("vendors", vendorRepository.findAll());
        model.addAttribute("selectedVendorId", vendorId);

        List<Product> products = (vendorId != null)
                ? productRepository.findAllProductsByVendorId(vendorId)
                : Collections.emptyList();
        model.addAttribute("products", products);

        // Backing object: prefill vendorId so th:field will render a value
        RestockForm req = (RestockForm) model.getAttribute("req");
        if (req == null) {
            req = new RestockForm();
            req.setVendorId(vendorId);
            req.setRequestedQty(1); // sensible default
            model.addAttribute("req", req);
        }

        model.addAttribute("isEdit", false); // create mode
        return "restock-form";
    }

    /* ===========================
       CREATE (POST)
       =========================== */
    @PostMapping
    public String create(@Valid @ModelAttribute("req") RestockForm req,
                         BindingResult binding,
                         Authentication auth,
                         Model model,
                         RedirectAttributes ra) {

        // Bean validation catches nulls/min; ensure explicit messages if needed
        if (req.getVendorId() == null) {
            binding.rejectValue("vendorId", "NotNull", "Vendor cannot be null");
        }
        if (req.getProductId() == null) {
            binding.rejectValue("productId", "NotNull", "Product cannot be null");
        }

        if (binding.hasErrors()) {
            rehydrateForCreate(model, req);
            return "restock-form";
        }

        // Load entities
        Product product = productRepository.findById(req.getProductId()).orElse(null);
        Vendor vendor = vendorRepository.findById(req.getVendorId()).orElse(null);

        if (product == null) {
            binding.rejectValue("productId", "Invalid", "Invalid product");
        }
        if (vendor == null) {
            binding.rejectValue("vendorId", "Invalid", "Invalid vendor");
        }
        if (binding.hasErrors()) {
            rehydrateForCreate(model, req);
            return "restock-form";
        }

        // Guard: product must belong to vendor
        boolean linked = vendorRepository.existsByVendorIdAndProducts_ProductId(
                vendor.getVendorId(), product.getProductId());
        if (!linked) {
            binding.rejectValue("productId", "Mismatch",
                    "Selected product is not supplied by the chosen vendor");
            rehydrateForCreate(model, req);
            return "restock-form";
        }

        // Save request
        RestockRequest rr = new RestockRequest();
        rr.setProduct(product);
        rr.setVendor(vendor);
        rr.setRequestedQty(req.getRequestedQty());
        rr.setStatus(RestockStatus.PENDING);
        rr.setCreatedByUsername(auth != null ? auth.getName() : "system");
        restockRequestRepository.save(rr);

        ra.addFlashAttribute("msg", "Restock request submitted!");
        return "redirect:/dashboard";
    }

    /* ===========================
       EDIT (GET)
       =========================== */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        RestockRequest rr = restockRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restock not found: " + id));

        RestockForm form = new RestockForm();
        form.setId(rr.getId());
        // Prefer business IDs if you have them; adjust to your entity getters:
        form.setProductId(rr.getProduct().getProductId());
        form.setVendorId(rr.getVendor().getVendorId());
        form.setRequestedQty(rr.getRequestedQty());

        Long vendorBusinessId = rr.getVendor().getVendorId();

        model.addAttribute("vendors", vendorRepository.findAll());
        model.addAttribute("selectedVendorId", vendorBusinessId);
        model.addAttribute("products",
                vendorBusinessId != null
                        ? productRepository.findAllProductsByVendorId(vendorBusinessId)
                        : Collections.emptyList());
        model.addAttribute("req", form);
        model.addAttribute("isEdit", true); // edit mode

        return "restock-form";
    }

    /* ===========================
       UPDATE (POST)
       =========================== */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("req") RestockForm req,
                         BindingResult binding,
                         RedirectAttributes ra,
                         Model model) {
        RestockRequest rr = restockRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restock not found: " + id));

        if (req.getVendorId() == null) {
            binding.rejectValue("vendorId", "NotNull", "Vendor cannot be null");
        }
        if (req.getProductId() == null) {
            binding.rejectValue("productId", "NotNull", "Product cannot be null");
        }

        if (binding.hasErrors()) {
            rehydrateForEdit(model, req);
            return "restock-form";
        }

        // Load entities
        Product product = productRepository.findById(req.getProductId()).orElse(null);
        Vendor vendor = vendorRepository.findById(req.getVendorId()).orElse(null);

        if (product == null) {
            binding.rejectValue("productId", "Invalid", "Invalid product");
        }
        if (vendor == null) {
            binding.rejectValue("vendorId", "Invalid", "Invalid vendor");
        }
        if (binding.hasErrors()) {
            rehydrateForEdit(model, req);
            return "restock-form";
        }

        // Guard: product must belong to vendor
        boolean linked = vendorRepository.existsByVendorIdAndProducts_ProductId(
                vendor.getVendorId(), product.getProductId());
        if (!linked) {
            binding.rejectValue("productId", "Mismatch",
                    "Selected product is not supplied by the chosen vendor");
            rehydrateForEdit(model, req);
            return "restock-form";
        }

        // Update entity (status/business rules unchanged)
        rr.setProduct(product);
        rr.setVendor(vendor);
        rr.setRequestedQty(req.getRequestedQty());
        restockRequestRepository.save(rr);

        ra.addFlashAttribute("msg", "Restock request updated");
        return "redirect:/dashboard";
    }

    /* ===========================
       DELETE
       =========================== */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        RestockRequest rr = restockRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restock not found: " + id));
        restockRequestRepository.delete(rr);
        ra.addFlashAttribute("msg", "Restock request deleted");
        return "redirect:/dashboard";
    }

    /* ===========================
       Helpers to rehydrate model
       =========================== */
    private void rehydrateForCreate(Model model, RestockForm req) {
        model.addAttribute("vendors", vendorRepository.findAll());
        model.addAttribute("selectedVendorId", req.getVendorId());
        model.addAttribute("products",
                req.getVendorId() != null
                        ? productRepository.findAllProductsByVendorId(req.getVendorId())
                        : Collections.emptyList());
        model.addAttribute("isEdit", false);
    }

    private void rehydrateForEdit(Model model, RestockForm req) {
        model.addAttribute("vendors", vendorRepository.findAll());
        model.addAttribute("selectedVendorId", req.getVendorId());
        model.addAttribute("products",
                req.getVendorId() != null
                        ? productRepository.findAllProductsByVendorId(req.getVendorId())
                        : Collections.emptyList());
        model.addAttribute("isEdit", true);
    }

    /* ===========================
       FORM DTO
       =========================== */
    public static class RestockForm {
        private Long id;

        @NotNull(message = "Product is required")
        private Long productId;

        @NotNull(message = "Vendor cannot be null")
        private Long vendorId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer requestedQty;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public Long getVendorId() { return vendorId; }
        public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

        public Integer getRequestedQty() { return requestedQty; }
        public void setRequestedQty(Integer requestedQty) { this.requestedQty = requestedQty; }
    }
}