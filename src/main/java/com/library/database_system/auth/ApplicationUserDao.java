package com.library.database_system.auth;

import com.library.database_system.auth.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByUsername(String Username);
}
