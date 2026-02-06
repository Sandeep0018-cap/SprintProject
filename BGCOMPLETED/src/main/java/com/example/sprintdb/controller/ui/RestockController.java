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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("vendors", vendorRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("req", new RestockForm());
        return "restock-form";
    }

    @PostMapping
    public String create(@ModelAttribute("req") RestockForm req,
                         Authentication auth,
                         RedirectAttributes ra) {
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product"));
        Vendor vendor = vendorRepository.findById(req.getVendorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid vendor"));

        RestockRequest rr = new RestockRequest();
        rr.setProduct(product);
        rr.setVendor(vendor);
        rr.setRequestedQty(req.getRequestedQty());
        rr.setStatus(RestockStatus.PENDING);
        rr.setCreatedByUsername(auth != null ? auth.getName() : "system");

        restockRequestRepository.save(rr);

        ra.addFlashAttribute("msg",
                "Restock request submitted for " + product.getBrand() + " " + product.getName());

        return "redirect:/dashboard";
    }

    public static class RestockForm {
        @NotNull private Long productId;
        @NotNull private Long vendorId;
        @NotNull @Min(1) private Integer requestedQty;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Long getVendorId() { return vendorId; }
        public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
        public Integer getRequestedQty() { return requestedQty; }
        public void setRequestedQty(Integer requestedQty) { this.requestedQty = requestedQty; }
    }
}