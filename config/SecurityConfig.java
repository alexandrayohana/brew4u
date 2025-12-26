package com.impal.demo_brew4u.config;

import com.impal.demo_brew4u.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler; // Handler redirect Admin/User

    @Autowired
    private CustomUserDetailsService userDetailsService; // Service pengecekan DB

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/", "/menu", "/register", "/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/keranjang/**", "/pembayaran/**", "/profile/**").hasAnyRole("PELANGGAN", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Halaman Login
                
                // PENTING: Ini yang menangkap POST dari form HTML
                .loginProcessingUrl("/login") 
                
                // PENTING: Karena di HTML name="email"
                .usernameParameter("email") 
                
                .successHandler(successHandler) // Redirect setelah sukses
                .failureUrl("/login?error=true") // Redirect jika gagal
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Matikan CSRF untuk mempermudah

        return http.build();
    }
}
