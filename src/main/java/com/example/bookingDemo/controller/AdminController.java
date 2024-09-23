package com.example.bookingDemo.controller;

import com.example.bookingDemo.service.admin.UserMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserMaintenanceService userMaintenanceService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminDashboard(){
        return ResponseEntity.ok("Welcome to the admin dashboard");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        userMaintenanceService.deleteUser(userId);

        return ResponseEntity.ok("User: " + userId + ", has been deleted");
    }
}
