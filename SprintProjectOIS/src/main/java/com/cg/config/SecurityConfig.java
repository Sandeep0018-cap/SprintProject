package com.cg.config; // Defines the package for security configuration

import org.springframework.context.annotation.Bean; // Import for defining Spring Beans
import org.springframework.context.annotation.Configuration; // Marks class as a configuration source
import org.springframework.security.config.Customizer; // Utility for default security configurations
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Enables method-level security annotations
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Configures web-based security filters
import org.springframework.security.core.userdetails.User; // Represents a Spring Security user entity
import org.springframework.security.core.userdetails.UserDetailsService; // Interface to retrieve user-specific data
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCrypt implementation for hashing
import org.springframework.security.crypto.password.PasswordEncoder; // Interface for password encoding logic
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // Manager for in-memory user storage
import org.springframework.security.web.SecurityFilterChain; // Defines the matching filter chain for requests

@Configuration // Marks this class as a Spring configuration component
@EnableMethodSecurity // Activates @PreAuthorize and @Secured annotations
public class SecurityConfig { // Main security configuration class

    @Bean // Registers the security filter chain as a Spring Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http // Returns the built configuration
            .authorizeHttpRequests(auth -> auth // Defines URL-based authorization rules
                .antMatchers("/login", "/css/**", "/js/**").permitAll() // Allows public access to login and static files
                .antMatchers("/staff/**").hasRole("STAFF") // Restricts /staff path to STAFF role
                .antMatchers("/admin/**").hasRole("ADMIN") // Restricts /admin path to ADMIN role
                .anyRequest().authenticated() // Requires authentication for all other requests
            )
            .formLogin(form -> form // Configures standard form-based login
                .loginPage("/login").permitAll() // Sets the custom login page path
                .defaultSuccessUrl("/dashboard", true) // Forces redirect to dashboard after login
            )
            .logout(logout -> logout // Configures the logout process
                .logoutUrl("/logout") // Sets the logout trigger URL
                .logoutSuccessUrl("/login?logout") // Redirects to login with a logout parameter
            )
            .csrf(Customizer.withDefaults()) // Enables default CSRF protection
            .build(); // Finalizes the HttpSecurity configuration
    }

    @Bean // Registers the service that manages user credentials
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Creates and stores users in memory for authentication
        return new InMemoryUserDetailsManager(
            User.withUsername("admin") // Defines admin username
                .password(encoder.encode("admin123")) // Encodes the admin password
                .roles("ADMIN").build(), // Assigns ADMIN role
            User.withUsername("staff") // Defines staff username
                .password(encoder.encode("staff123")) // Encodes the staff password
                .roles("STAFF").build() // Assigns STAFF role
        );
    }

    @Bean // Registers the hashing algorithm for the application
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Uses BCrypt for secure one-way hashing
    }
}
