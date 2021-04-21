package com.library.database_system.auth;

import com.library.database_system.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.library.database_system.security.ApplicationUserRole.ADMIN;

@Repository("admin")
public class ApplicationAdminDaoService implements ApplicationUserDao{

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Autowired
    public ApplicationAdminDaoService(PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    // set users
    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = adminRepository.findAll().stream()
                        .map(admin -> new ApplicationUser(admin.getUsername(),
                                        admin.getPassword(), // already encoded
                                        ADMIN.getGrantedAuthority(),
                                        true,
                                        true,
                                        true,
                                        true))
                        .collect(Collectors.toList());

        return applicationUsers;
    }
}
