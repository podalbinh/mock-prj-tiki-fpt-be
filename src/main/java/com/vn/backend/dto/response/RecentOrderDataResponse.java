package com.vn.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentOrderDataResponse {
    private String name; // "Hôm nay", "Hôm qua"...
    private Long orders;
    private Double revenue;
}
