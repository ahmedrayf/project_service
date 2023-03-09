package com.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project_category")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE ProjectCategory SET deleted = '1' WHERE id=?")
@Where(clause = "deleted=false")
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_category_id")
    private List<Project> projects;

    @JsonIgnore
    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;

}
