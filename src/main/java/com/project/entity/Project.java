package com.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "You have to set Project name")
    private String name;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "project_manager_id")
    private Long projectManagerId;
    @Column(name = "description")
    private String description;
    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @Column(name = "project_status")
    @NotNull(message = "You have to set Project Status")
    private int projectStatus;

    @Column(name = "project_category_id")
    private Long projectCategoryId;

    @OneToMany
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private List<ProjectMember> projectMembers;


}
