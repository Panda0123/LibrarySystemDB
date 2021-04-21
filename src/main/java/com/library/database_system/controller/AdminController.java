package com.library.database_system.controller;

import com.library.database_system.dtos.AdminDTO;
import com.library.database_system.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping(path = "/id")
    public Long getId(@RequestBody String username) {
        return this.adminService.getId(username);
    }

    @PostMapping()
    public void updateAdmin(@RequestBody AdminDTO credentials) {
        this.adminService.updateAdmin(credentials);
    }
}
