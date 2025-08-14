package com.vn.backend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponse {
    private DashboardStatsResponse statsData;
    private List<MonthlyDataResponse> monthlyData;
    private List<CategoryDataResponse> categoryData;
    private List<RecentOrderDataResponse> recentOrdersData;
}
