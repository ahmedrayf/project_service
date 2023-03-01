package com.project.service;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.entity.Project;
import com.project.entity.ProjectCategory;
import com.project.repo.ProjectRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private CategoryService categoryService;

    public Project getProjectById(Long id) {
        Optional<Project> project = projectRepo.findById(id);
        if (!project.isEmpty())
            throw new RuntimeException();
        return project.get();
    }

    public Page<Project> getAllProjects(String searchTag , PagingInfo pagingInfo) {

        Page<Project> projects;
        Pageable pageable = PageRequest.of(pagingInfo.getPageNum() - 1, pagingInfo.getCount());

        if (searchTag != null&& !searchTag.isEmpty() )
            projects = projectRepo.findByNameContaining(searchTag , pageable);
        else
            projects = projectRepo.findAll(pageable);
        return projects;
    }

    public Project addProject(ProjectDTO projectDTO) {

        ProjectCategory projectCategory = categoryService.getCategoryById(projectDTO.getProjectCategoryId());

        Project project = Project.builder()
                .name(projectDTO.getName())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .projectCategory(projectCategory)
                .addedDate(LocalDateTime.now())
                .description(projectDTO.getDescription())
                .projectStatus(projectDTO.getProjectStatus())
                .build();

        projectRepo.save(project);
        return project;
    }

    public Project editProject(Long id, ProjectDTO projectDTO) {

        Optional<Project> project = projectRepo.findById(id);
        if (!project.isPresent())
            throw new RuntimeException();

        ProjectCategory projectCategory = categoryService.getCategoryById(projectDTO.getProjectCategoryId());

        Project updatedProject = Project.builder()
                .id(id)
                .name(projectDTO.getName())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .projectCategory(projectCategory)
                .addedDate(LocalDateTime.now())
                .description(projectDTO.getDescription())
                .projectStatus(projectDTO.getProjectStatus())
                .build();


        projectRepo.save(updatedProject);
        return updatedProject;
    }
}
