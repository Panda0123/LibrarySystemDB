package com.library.database_system.repository;

import com.library.database_system.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByUsername(String username);

    List<Admin> findAll();

    Optional<Admin> findAdminById(Long id);
}
