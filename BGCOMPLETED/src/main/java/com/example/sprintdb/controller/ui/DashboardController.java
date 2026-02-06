package com.example.sprintdb.controller.ui;

import com.example.sprintdb.dto.ProductStockAlertDto;
import com.example.sprintdb.entity.*;
import com.example.sprintdb.repository.OrderRepository;
import com.example.sprintdb.repository.ProductRepository;
import com.example.sprintdb.repository.PurchaseItemRepository;
import com.example.sprintdb.repository.RestockRequestRepository;
import com.example.sprintdb.service.ProductService;
import com.example.sprintdb.service.PurchaseService;
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

    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final RestockRequestRepository restockRequestRepository;

    public DashboardController(PurchaseService purchaseService,
                               ProductService productService,
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

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth, HttpSession session) {
        boolean isAdmin = hasRole(auth, "ROLE_ADMIN");
        boolean isStaff = hasRole(auth, "ROLE_STAFF");

        // Welcome overlay (once per session, no redirect)
        if (session.getAttribute("WELCOME_SHOWN") == null) {
            session.setAttribute("WELCOME_SHOWN", Boolean.TRUE);
            model.addAttribute("showWelcome", true);
            model.addAttribute("welcomeRole", isAdmin ? "Welcome Admin" : (isStaff ? "Welcome Staff" : "Welcome"));
        }
        model.addAttribute("loginRole", isAdmin ? "Admin Login" : (isStaff ? "Staff Login" : "User"));

        // Alerts (every refresh)
        List<ProductStockAlertDto> alerts = productService.lowStockSummary(10);
        model.addAttribute("lowStockAlerts", alerts);
        model.addAttribute("lowStockCount", alerts.size());

        // Order status chart
        Map<String, Long> orderStatusCounts = new LinkedHashMap<>();
        for (OrderStatus s : OrderStatus.values()) orderStatusCounts.put(s.name(), 0L);
        orderRepository.findAll().forEach(o ->
                orderStatusCounts.computeIfPresent(o.getStatus().name(), (k,v)->v+1));
        model.addAttribute("orderStatusLabels", new ArrayList<>(orderStatusCounts.keySet()));
        model.addAttribute("orderStatusValues", new ArrayList<>(orderStatusCounts.values()));

        // Monthly revenue (last 12)
        YearMonth thisMonth = YearMonth.now();
        List<YearMonth> months = new ArrayList<>();
        for (int i = 11; i >= 0; i--) months.add(thisMonth.minusMonths(i));
        Map<YearMonth, Double> revenueByMonth = months.stream()
                .collect(Collectors.toMap(m -> m, m -> 0.0, (a,b)->a, LinkedHashMap::new));

        List<PurchaseItem> allItems = purchaseItemRepository.findAll();
        allItems.forEach(pi -> {
            if (pi.getPurchase() == null || pi.getPurchase().getCreatedAt() == null) return;
            LocalDate dt = pi.getPurchase().getCreatedAt().toLocalDate();
            YearMonth ym = YearMonth.from(dt);
            if (revenueByMonth.containsKey(ym)) {
                double add = nz(pi.getUnitPrice()) * nzi(pi.getQuantity());
                revenueByMonth.put(ym, revenueByMonth.get(ym) + add);
            }
        });
        model.addAttribute("monthsLabels", revenueByMonth.keySet().stream()
                .map(ym -> ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear())
                .collect(Collectors.toList()));
        model.addAttribute("monthsValues", new ArrayList<>(revenueByMonth.values()));

        // Brand revenue
        Map<String, Double> revenueByBrand = new LinkedHashMap<>();
        allItems.forEach(pi -> {
            if (pi.getProduct() == null) return;
            String brand = Optional.ofNullable(pi.getProduct().getBrand()).orElse("Unknown");
            revenueByBrand.merge(brand, nz(pi.getUnitPrice()) * nzi(pi.getQuantity()), Double::sum);
        });
        LinkedHashMap<String, Double> brandSorted = revenueByBrand.entrySet().stream()
                .sorted(Map.Entry.<String,Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a,b)->a, LinkedHashMap::new));
        model.addAttribute("brandLabels", new ArrayList<>(brandSorted.keySet()));
        model.addAttribute("brandValues", new ArrayList<>(brandSorted.values()));

        // Stock donut buckets
        Map<Long, Integer> soldByProduct = allItems.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getId(),
                        Collectors.summingInt(PurchaseItem::getQuantity)));
        int red=0, orange=0, green=0;
        for (Product p : productRepository.findAll()) {
            int stock = p.getStockQty() == null ? 0 : p.getStockQty();
            int sold = soldByProduct.getOrDefault(p.getId(), 0);
            int available = Math.max(0, stock - sold);
            if (available <= 4) red++;
            else if (available <= 10) orange++;
            else green++;
        }
        model.addAttribute("donutLabels", Arrays.asList("0–4 (Low)", "5–10 (Med)", "≥10 (OK)"));
        model.addAttribute("donutValues", Arrays.asList(red, orange, green));

        // Payments (Admin dashboard only)
        Map<String, Long> raw = purchaseService.paymentModeCounts();
        Map<String, Long> modeCounts = new LinkedHashMap<>();
        for (PaymentMode m : PaymentMode.values()) {
            modeCounts.put(m.name(), raw.getOrDefault(m.name(), 0L));
        }
        model.addAttribute("modeCounts", modeCounts);

        // Staff: own restocks
        if (isStaff) {
            String username = auth != null ? auth.getName() : null;
            model.addAttribute("myRestocks",
                    username == null ? Collections.emptyList() :
                            restockRequestRepository.findAll().stream()
                                    .filter(r -> username.equals(r.getCreatedByUsername()))
                                    .sorted(Comparator.comparingLong(r -> -r.getId()))
                                    .collect(Collectors.toList()));
        }

        return "dashboard";
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(role::equals);
    }
    private double nz(Double d){ return d==null?0.0:d; }
    private int nzi(Integer i){ return i==null?0:i; }
}