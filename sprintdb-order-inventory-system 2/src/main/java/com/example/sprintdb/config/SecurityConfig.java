package com.example.sprintdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails staff = User.withUsername("staff")
                .password(encoder.encode("staff123"))
                .roles("STAFF")
                .build();

        return new InMemoryUserDetailsManager(admin, staff);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/login").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/staff/**").hasAnyRole("STAFF", "ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();

        return http.build();
    }
}
