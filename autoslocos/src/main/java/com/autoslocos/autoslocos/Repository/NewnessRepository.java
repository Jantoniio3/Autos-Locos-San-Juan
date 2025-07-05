package com.autoslocos.autoslocos.Repository;

import com.autoslocos.autoslocos.Entity.Newness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewnessRepository extends JpaRepository<Newness, Long> {
}
