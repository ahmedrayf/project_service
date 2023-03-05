package com.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.entity.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCategoryDTO {

    @NotBlank(message = "You have to set Project category name")
    private String name;
    private String description;
    private Set<Project> projects;

}
