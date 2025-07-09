package com.autoslocos.autoslocos.Security;

import com.autoslocos.autoslocos.Entity.Admin;
import com.autoslocos.autoslocos.Repository.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Admin no encontrado: " + username));

        return User.builder()
            .username(admin.getUsername())
            .password(admin.getPassword())
            .roles("ADMIN") // Asegúrate de que coincida con lo que usas en SecurityConfig
            .build();
    }
}