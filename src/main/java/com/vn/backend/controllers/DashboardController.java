package com.vn.backend.controllers;

import com.vn.backend.dto.response.AdminDashboardResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get admin dashboard data
     * 
     * @return AdminDashboardResponse with all dashboard data
     */
    @GetMapping
    public ResponseData<AdminDashboardResponse> getAdminDashboard() {
        try {
            AdminDashboardResponse dashboardData = dashboardService.getAdminDashboardData();
            return ResponseData.success(dashboardData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get admin dashboard data", e);
        }
    }
}
