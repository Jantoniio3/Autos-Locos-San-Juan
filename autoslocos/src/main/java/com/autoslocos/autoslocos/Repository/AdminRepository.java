package com.autoslocos.autoslocos.Repository;

import com.autoslocos.autoslocos.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}