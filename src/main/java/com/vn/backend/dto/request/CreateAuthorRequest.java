package com.vn.backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuthorRequest {
    private String name;
    private String slug;
}
