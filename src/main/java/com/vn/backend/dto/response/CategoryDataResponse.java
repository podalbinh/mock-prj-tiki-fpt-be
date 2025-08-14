package com.vn.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDataResponse {
    private String name;
    private Double value; // percentage
}
