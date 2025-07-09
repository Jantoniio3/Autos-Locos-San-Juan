package com.autoslocos.autoslocos.Repository;

import com.autoslocos.autoslocos.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username); // ¡Este método es crucial!

}