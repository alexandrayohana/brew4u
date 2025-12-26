package com.impal.demo_brew4u.config;

import org.springframework.context.annotation.Configuration;

import com.midtrans.Midtrans;

import jakarta.annotation.PostConstruct;

@Configuration
public class MidtransConfig {

    // =====================================================================
    // KUNCI INI DIAMBIL DARI SCREENSHOT DASHBOARD ANDA (image_531284.png)
    // =====================================================================
    
    // Server Key
    public static final String SERVER_KEY = "Mid-server-hxFdiHyYrYSTDQ45XFTnBaFP"; 

    // Client Key
    public static final String CLIENT_KEY = "Mid-client-pwmSqUcVMnz8brGl"; 

    // PENTING:
    // 1. Karena key Anda berawalan "Mid-" (bukan "SB-Mid-"), library mungkin menganggapnya Production.
    // 2. Jika "false" masih error 401, coba ubah ini menjadi "true".
    public static final boolean IS_PRODUCTION = false; 

    @PostConstruct
    public void init() {
        Midtrans.serverKey = SERVER_KEY;
        Midtrans.clientKey = CLIENT_KEY;
        Midtrans.isProduction = IS_PRODUCTION;
        
        // Debugging di Terminal untuk memastikan Config terbaca
        System.out.println("--------------------------------------------------");
        System.out.println("Midtrans Configuration Loaded!");
        System.out.println("Server Key: " + SERVER_KEY);
        System.out.println("Client Key: " + CLIENT_KEY);
        System.out.println("Is Production: " + IS_PRODUCTION);
        System.out.println("--------------------------------------------------");
    }
}
