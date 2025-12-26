package com.impal.demo_brew4u.repositories;

import com.impal.demo_brew4u.models.Cabang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabangRepository extends JpaRepository<Cabang, Long> {
    
}