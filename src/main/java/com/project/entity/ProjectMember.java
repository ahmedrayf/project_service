package com.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMember {

    @Id
    private Long projectId;
    private Long userId;
    private LocalDateTime addedDate;
    private String addedBy;



}
