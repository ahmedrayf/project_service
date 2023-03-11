package com.project.dto;

import com.project.entity.ProjectMember;
import com.project.enums.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    @NotNull(message = "You have to set Project name")
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @NotNull
    private Long projectCategoryId;
    @Min(1)
    private Long projectManagerId;
    private String description;
    @NotNull(message = "You have to set Project Status")
    @Min(value = 1)
    @Max(value = 3)
    private int projectStatus;

}
