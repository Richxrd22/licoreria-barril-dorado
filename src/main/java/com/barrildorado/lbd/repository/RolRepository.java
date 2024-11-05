package com.barrildorado.lbd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barrildorado.lbd.model.Rol;
@Repository
public interface RolRepository extends JpaRepository<Rol,Long>{
    
}
