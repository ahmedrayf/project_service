package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingInfo {
    private int pageNum;
    private int count;//size=10 per page
    private boolean includeTotalCount;
    private List<String> sortBy;
    private int direction;//(1=ASC)  (-1=DESC)

}
