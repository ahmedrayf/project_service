package com.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_member")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_Name")
    @NotNull(message = "You have to set username")
    private String userName;
    @Column(name = "project_id")
    @NotNull(message = "You have to set project_id")
    private Long projectId;
    @Column(name = "added_date")
    private LocalDateTime addedDate;
    @Column(name = "added_by")
    private String addedBy;



}
