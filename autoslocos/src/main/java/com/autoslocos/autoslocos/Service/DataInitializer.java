package com.autoslocos.autoslocos.Service;

import com.autoslocos.autoslocos.Entity.Admin;
import com.autoslocos.autoslocos.Entity.Sponsor;
import com.autoslocos.autoslocos.Repository.AdminRepository;
import com.autoslocos.autoslocos.Repository.SponsorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepo, SponsorRepository sponsorRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "administracion2025";
            String rawPassword = "AutosLocos@2025"; // Cambia esto en producción

            // Crear administrador si no existe
            if (adminRepo.findByUsername(adminUsername).isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername(adminUsername);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                admin.setPassword(encoder.encode(rawPassword));
                adminRepo.save(admin);
                System.out.println("Usuario administrador creado: " + adminUsername);
            }

            // Lista de sponsors
            List<String> sponsorNames = List.of(
                    "Ayuntamiento San Juan de la Nava", "Gaman Mobility", "Prime Energy", "CYC Vías", "Lamantis Performance",
                    "Transportes y Excavaciones Rodrigo", "Carnicería El Músico", "Carpintería Caraballo",
                    "Prao Cabo", "Cerebellium Apps", "Transportes y Excavaciones Galo",
                    "Clínica de Logopedia Patricia López", "Taller Jairo García", "JM Seguros",
                    "Huevos Redondo", "Taller Chapa y Pintura RG", "Hijos de Justo", "RMC, S.L.",
                    "Pintores Carlos", "Carpintería y Ebanistería Hermanos Yuste", "Carnes Rafa González",
                    "Administración de Lotería La Churrería", "Cocinas Aparicio Gómez",
                    "Bar Restaurante La Piscina", "Hormigones Menga",
                    "Malafama Barber", "BAR NKJAVI", "Electricidad Peral","Pilla un Pollo"
            );

            for (String name : sponsorNames) {
                if (sponsorRepo.findAll().stream().noneMatch(s -> s.getName().equalsIgnoreCase(name))) {
                    Sponsor sponsor = new Sponsor();
                    sponsor.setName(name);

                    // Asumimos que las imágenes tienen el mismo nombre que el sponsor, sin espacios y en minúsculas, con .jpg
                    String imageFileName = name.toLowerCase()
                                               .replace("á", "a")
                                               .replace("é", "e")
                                               .replace("í", "i")
                                               .replace("ó", "o")
                                               .replace("ú", "u")
                                               .replace("ñ", "n")
                                               .replaceAll("[^a-z0-9]", "") + ".jpg";

                    sponsor.setLogo(loadImage(imageFileName));
                    sponsorRepo.save(sponsor);
                    System.out.println("Sponsor creado: " + name);
                }
            }
        };
    }

    private byte[] loadImage(String imageName) {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("static/assets/images/sponsors/" + imageName)) {
            if (inputStream == null) {
                System.err.println("No se encontró la imagen: " + imageName);
                return new byte[0];
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
