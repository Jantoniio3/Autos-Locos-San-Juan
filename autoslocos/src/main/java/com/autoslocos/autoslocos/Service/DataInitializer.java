package com.autoslocos.autoslocos.Service;

import com.autoslocos.autoslocos.Entity.Admin;
import com.autoslocos.autoslocos.Repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepo) {
        return args -> {
            if (adminRepo.findByUsername("admin").isEmpty()) {
                Admin admin = new Admin("administracion2025", "AutosLocos_SJ_2025"); // La contraseña se encripta automáticamente
                adminRepo.save(admin);
            }
        };
    }
}