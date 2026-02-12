package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cg.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // CATEGORY PAGE — use Java properties: Category.categoryId → OK
    List<Product> findByCategoryCategoryIdOrderByBrandAscNameAsc(Long categoryId);

    // RESTOCK PAGE — all products for a vendor (JPQL uses property names)
    @Query("select p from Product p where p.vendor.vendorId = :vendorId")
    List<Product> findAllProductsByVendorId(@Param("vendorId") Long vendorId);

    // RESTOCK PAGE — low-stock products for a vendor
    @Query("select p from Product p " +
           "where p.vendor.vendorId = :vendorId " +
           "and p.stockQty <= :threshold " +
           "order by p.brand asc, p.name asc")
    List<Product> findLowStockProductsByVendorId(@Param("vendorId") Long vendorId,
                                                 @Param("threshold") Integer threshold);
}
