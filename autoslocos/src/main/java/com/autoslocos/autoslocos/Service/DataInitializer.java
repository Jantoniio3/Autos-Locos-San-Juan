package com.autoslocos.autoslocos.Service;  // Asegúrate que el paquete sea correcto

import com.autoslocos.autoslocos.Entity.Admin;
import com.autoslocos.autoslocos.Repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "administracion2025";
            String rawPassword = "AutosLocos@2025"; // Cambia esto en producción
            
            if (adminRepo.findByUsername(adminUsername).isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername(adminUsername);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                admin.setPassword(encoder.encode(rawPassword));


                
                adminRepo.save(admin);
                System.out.println("Usuario administrador creado: " + adminUsername);
            }
        };
    }
}