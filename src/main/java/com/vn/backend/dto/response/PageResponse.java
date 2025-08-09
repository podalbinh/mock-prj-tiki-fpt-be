package com.vn.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private int totalElements;
    private boolean last;
    private String sortBy;
    private String orderBy;
    private T content;

}
