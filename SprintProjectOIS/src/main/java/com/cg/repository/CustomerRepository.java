package com.cg.repository;
 
import com.cg.dto.CustomerRowDto;

import com.cg.entity.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
 
public interface CustomerRepository extends JpaRepository<Customer, Long> {
 
	@Query("""

		    SELECT new com.cg.dto.CustomerRowDto(

		      c.customer_id,

		      c.name,

		      c.phone,

		      c.city,

		      pr.name,

		      pr.brand,

		      p.paymentMode,

		      p.transactionId      

		    )

		    FROM Customer c

		    LEFT JOIN Purchase p ON p.customer.customer_id = c.customer_id

		    LEFT JOIN Product pr ON pr.productId = p.product.productId

		    WHERE p.createdAt = (

		      SELECT MAX(p2.createdAt)

		      FROM Purchase p2

		      WHERE p2.customer.customer_id = c.customer_id

		    )

		    ORDER BY c.customer_id DESC

		    """)

		List<CustomerRowDto> listCustomersWithLastPurchase();

}
 