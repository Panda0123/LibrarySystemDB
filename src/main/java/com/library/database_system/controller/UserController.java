package com.library.database_system.controller;


import com.library.database_system.domain.User;
import com.library.database_system.projections.UserProj;
import com.library.database_system.resulttransformer.UserTransformer;
import com.library.database_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("admin")
    public void addNewUser(User user) {
        this.userService.addUser(user);
    }

    @GetMapping("all")
    public List<UserTransformer> getUsers() {
        return this.userService.getUsersDetails();
    }

    @GetMapping("all/{userId}")
    public UserProj findUserDetails(@PathVariable("userId") String id) {
        return this.userService.findUserDetails(id);
    }

    @PutMapping(value = "admin/{userId}")
    public void updateUser(
            @PathVariable("userId") String id,
            @RequestParam(required = false) String fName,
            @RequestParam(required = false) String mName,
            @RequestParam(required = false) String lName,
            @RequestParam(required = false) String address
            ) {

        HashMap<String, String> attrs = new HashMap<>();
        if (fName != null)
            attrs.put("fName", fName);
        if (mName != null)
            attrs.put("mName", mName);
        if (lName != null)
            attrs.put("lName", lName);
        if (address != null)
            attrs.put("address", address);
        this.userService.updateUser(id, attrs);
    }
}
