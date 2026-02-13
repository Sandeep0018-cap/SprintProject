package com.cg.controller;

import com.cg.dto.ProductStockAlertDto;
import com.cg.entity.*;
import com.cg.enums.OrderStatus;
import com.cg.enums.PaymentMode;
import com.cg.repository.*;
import com.cg.service.IProductService;
import com.cg.service.IPurchaseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final IPurchaseService purchaseService;
    private final IProductService productService;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final RestockRequestRepository restockRequestRepository;

    public DashboardController(IPurchaseService purchaseService,
                               IProductService productService,
                               PurchaseItemRepository purchaseItemRepository,
                               ProductRepository productRepository,
                               OrderRepository orderRepository,
                               RestockRequestRepository restockRequestRepository) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.purchaseItemRepository = purchaseItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.restockRequestRepository = restockRequestRepository;
    }

    @GetMapping("/dashboard") // Orchestrates data for the primary analytical landing page
    public String dashboard(Model model, Authentication auth, HttpSession session) {
        boolean isAdmin = hasRole(auth, "ROLE_ADMIN"); // Checks for administrative privileges
        boolean isStaff = hasRole(auth, "ROLE_STAFF"); // Checks for staff-level access

        // Manages a one-time welcome message state per user session
        if (session.getAttribute("WELCOME_SHOWN") == null) {
            session.setAttribute("WELCOME_SHOWN", Boolean.TRUE);
            model.addAttribute("showWelcome", true);
            model.addAttribute("welcomeRole", isAdmin ? "Welcome Admin" : (isStaff ? "Welcome Staff" : "Welcome"));
        }
        model.addAttribute("loginRole", isAdmin ? "Admin Login" : (isStaff ? "Staff Login" : "User"));

        // Retrieves a list of products falling below the defined threshold
        List<ProductStockAlertDto> alerts = productService.lowStockSummary(10);
        model.addAttribute("lowStockAlerts", alerts);
        model.addAttribute("lowStockCount", alerts.size());

        // Aggregates counts for every possible order status for charting
        Map<String, Long> orderStatusCounts = new LinkedHashMap<>();
        Arrays.stream(OrderStatus.values()).forEach(s -> orderStatusCounts.put(s.name(), 0L));
        orderRepository.findAll().forEach(o ->
                orderStatusCounts.computeIfPresent(o.getStatus().name(), (k, v) -> v + 1));
        model.addAttribute("orderStatusLabels", new ArrayList<>(orderStatusCounts.keySet()));
        model.addAttribute("orderStatusValues", new ArrayList<>(orderStatusCounts.values()));

        // Initializes a 12-month rolling window for financial reporting
        YearMonth thisMonth = YearMonth.now();
        Map<YearMonth, Double> revenueByMonth = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) revenueByMonth.put(thisMonth.minusMonths(i), 0.0);

        List<PurchaseItem> allItems = purchaseItemRepository.findAll();
        allItems.forEach(pi -> {
            if (pi.getPurchase() == null || pi.getPurchase().getCreatedAt() == null) return;
            YearMonth ym = YearMonth.from(pi.getPurchase().getCreatedAt().toLocalDate());
            if (revenueByMonth.containsKey(ym)) {
                // Calculates item total with null-safety and updates monthly bucket
                double add = nz(pi.getUnitPrice()) * nzi(pi.getQuantity());
                revenueByMonth.put(ym, revenueByMonth.get(ym) + add);
            }
        });
        
        // Formats month keys into readable labels (e.g., "Jan 2024")
        model.addAttribute("monthsLabels", revenueByMonth.keySet().stream()
                .map(ym -> ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear())
                .collect(Collectors.toList()));
        model.addAttribute("monthsValues", new ArrayList<>(revenueByMonth.values()));

        // Calculates total revenue generated per manufacturer brand
        Map<String, Double> revenueByBrand = new HashMap<>();
        allItems.forEach(pi -> {
            String brand = (pi.getProduct() != null && pi.getProduct().getBrand() != null) ? pi.getProduct().getBrand() : "Unknown";
            revenueByBrand.merge(brand, nz(pi.getUnitPrice()) * nzi(pi.getQuantity()), Double::sum);
        });
        
        // Sorts brands by revenue descending to highlight top performers
        Map<String, Double> brandSorted = revenueByBrand.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
        model.addAttribute("brandLabels", new ArrayList<>(brandSorted.keySet()));
        model.addAttribute("brandValues", new ArrayList<>(brandSorted.values()));

        // Groups sales volume by product ID to determine real-time availability
        Map<Long, Integer> soldByProduct = allItems.stream()
                .filter(pi -> pi.getProduct() != null)
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getProductId(), Collectors.summingInt(PurchaseItem::getQuantity)));
        
        int red = 0, orange = 0, green = 0;
        for (Product p : productRepository.findAll()) {
            int available = nzi(p.getStockQty()) - soldByProduct.getOrDefault(p.getProductId(), 0);
            if (available <= 4) red++; // Critical stock level
            else if (available <= 10) orange++; // Warning stock level
            else green++; // Healthy stock level
        }
        model.addAttribute("donutLabels", Arrays.asList("0–4 (Low)", "5–10 (Med)", "≥10 (OK)"));
        model.addAttribute("donutValues", Arrays.asList(red, orange, green));

        // Populates counts for various payment methods (Card, Cash, etc.)
        Map<String, Long> raw = purchaseService.paymentModeCounts();
        Map<String, Long> modeCounts = new LinkedHashMap<>();
        Arrays.stream(PaymentMode.values()).forEach(m -> modeCounts.put(m.name(), raw.getOrDefault(m.name(), 0L)));
        model.addAttribute("modeCounts", modeCounts);

        // Filters and sorts restock requests specifically created by the logged-in staff member
        if (isStaff) {
            String username = auth != null ? auth.getName() : null;
            model.addAttribute("myRestocks", username == null ? Collections.emptyList() :
                    restockRequestRepository.findAll().stream()
                            .filter(r -> username.equals(r.getCreatedByUsername()))
                            .sorted(Comparator.comparingLong(RestockRequest::getId).reversed())
                            .collect(Collectors.toList()));
        }

        return "dashboard";
    }

    private boolean hasRole(Authentication auth, String role) { // Validates user permissions against a specific role string
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private double nz(Double d) { return d == null ? 0.0 : d; } // "Null to Zero" for Double values
    private int nzi(Integer i) { return i == null ? 0 : i; }   // "Null to Zero" for Integer values
}
