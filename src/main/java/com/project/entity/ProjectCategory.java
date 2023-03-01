package com.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProjectCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name")
    @NotNull(message = "You have to set Project category name")
    private String Name;
    @Column(name = "Description")
    private String description;
    @Column(name = "AddedDate")
    private LocalDateTime addedDate;

    @OneToMany
    private Set<Project> projects;

}
