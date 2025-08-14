package com.vn.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyDataResponse {
    private String name; // T1, T2, T3...
    private Long users;
    private Long orders;
    private Double revenue;
    private Long books;
}
