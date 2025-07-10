package com.autoslocos.autoslocos.Repository;

import com.autoslocos.autoslocos.Entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    // Puedes agregar métodos personalizados aquí si los necesitas
}
