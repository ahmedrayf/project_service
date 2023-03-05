package com.project.dto;

import com.project.entity.ProjectMember;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
//    private Long projectManagerId;
        private String description;
    @NotNull(message = "You have to set Project Status")
    @Min(value = 1,message = "Insert a correct value for project status")
    @Max(value = 3,message = "Insert a correct value for project status")
    private int projectStatus;

}
