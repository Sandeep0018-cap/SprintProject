package com.example.sprintdb.config;

import com.example.sprintdb.entity.Vendor;
import com.example.sprintdb.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner seedVendors(VendorRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Vendor v1 = new Vendor(); v1.setName("Dell India"); v1.setContactName("Anil"); v1.setPhone("022-555-0100"); v1.setEmail("sales@dell.example");
                Vendor v2 = new Vendor(); v2.setName("HP Distribution"); v2.setContactName("Priya"); v2.setPhone("022-555-0101"); v2.setEmail("orders@hp.example");
                Vendor v3 = new Vendor(); v3.setName("Logitech Partners"); v3.setContactName("Rahul"); v3.setPhone("022-555-0102"); v3.setEmail("supply@logitech.example");
                repo.save(v1); repo.save(v2); repo.save(v3);
            }
        };
    }
}