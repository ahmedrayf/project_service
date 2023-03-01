package com.project.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class PageableResponse extends BaseResponse{
    
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int currentItems;

}
