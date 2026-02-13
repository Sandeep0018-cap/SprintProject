package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.cg.entity.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Derived query: Fetches items by category with secondary sorting by manufacturer then name
    List<Product> findByCategoryCategoryIdOrderByBrandAscNameAsc(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.vendor.vendorId = :vendorId") // Custom JPQL: Retrieves all items associated with a specific supplier ID
    List<Product> findAllProductsByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT p FROM Product p WHERE p.vendor.vendorId = :vendorId AND p.stockQty <= :threshold ORDER BY p.brand ASC, p.name ASC") // Logic: Filters for items belonging to a vendor that have fallen below a specific inventory limit
    List<Product> findLowStockProductsByVendorId(@Param("vendorId") Long vendorId,
                                                 @Param("threshold") Integer threshold);
}
