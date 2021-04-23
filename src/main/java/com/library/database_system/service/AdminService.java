package com.library.database_system.service;

import com.library.database_system.auth.ApplicationUserService;
import com.library.database_system.domain.Admin;
import com.library.database_system.dtos.AdminDTO;
import com.library.database_system.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final ApplicationUserService applicationUserService;

    public AdminService(PasswordEncoder passwordEncoder, AdminRepository adminRepository, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.applicationUserService = applicationUserService;
    }

    public void addNewAdmin(Admin admin ) {
        Optional<Admin> optionalAdmin = this.adminRepository
                .findAdminByUsername( admin.getUsername());
        if (optionalAdmin.isPresent()) {
            throw new IllegalStateException(String.format("Username %s already exist", admin.getUsername()));
        } else {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));  // encode the password
            this.adminRepository.save(admin);
        }
    }

    public boolean contain(Admin admin) {
        Optional<Admin> adminOpt = this.adminRepository.findAdminByUsername(admin.getUsername());
        return adminOpt.isPresent();
   }

    public Long getId(String username) {
        Optional<Admin> optionalAdmin = this.adminRepository
                .findAdminByUsername(username);
        if (optionalAdmin.isPresent())
            return optionalAdmin.get().getId();
        else
            return -1L;
    }

    @Transactional
    public void updateAdmin(AdminDTO adminDTO) {
        Admin admin = this.adminRepository.findAdminById(adminDTO.getId()).orElseThrow(
                () -> { return new IllegalStateException(String.format("AdminID:%s does not exist", adminDTO.getId()));});
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
    }

    public boolean isEmpty() {
        return this.adminRepository.findAll().isEmpty();
    }
}
