package com.project.service;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.dto.ProjectUsersDTO;
import com.project.entity.Project;
import com.project.entity.ProjectCategory;
import com.project.entity.ProjectMember;
import com.project.exception.NotFoundException;
import com.project.repo.ProjectMembersRepo;
import com.project.repo.ProjectRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProjectMembersRepo projectMembersRepo;

    public Project getProjectById(Long id) {
        Optional<Project> optionalProject = projectRepo.findById(id);
        if (!optionalProject.isEmpty())
            throw new NotFoundException("No such project for id: " + id);
        return optionalProject.get();
    }

    public Page<Project> getAllProjects(String searchTag, PagingInfo pagingInfo) {

        Page<Project> projects;
        Pageable pageable = PageRequest.of(pagingInfo.getPageNum() - 1, pagingInfo.getCount());

        if (searchTag != null && !searchTag.isEmpty())
            projects = projectRepo.findByNameContaining(searchTag, pageable);
        else
            projects = projectRepo.findAll(pageable);
        return projects;
    }

    public Project addProject(ProjectDTO projectDTO) {

        categoryService.getCategoryById(projectDTO.getProjectCategoryId());

        Project project = Project.builder()
                .name(projectDTO.getName())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .projectCategoryId(projectDTO.getProjectCategoryId())
                .addedDate(LocalDateTime.now())
                .description(projectDTO.getDescription())
                .projectStatus(projectDTO.getProjectStatus())
                .build();

        projectRepo.save(project);
        return project;
    }

    private List<ProjectMember> findProjectMembers(List<Long> projectMembersIds) {
        Iterable projectMembers;
        try {
            projectMembers = projectMembersRepo.findAllById(projectMembersIds);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
        return (List<ProjectMember>) projectMembers;
    }

    public Project editProject(Long id, ProjectDTO projectDTO) {

        Optional<Project> optionalProject = projectRepo.findById(id);
        if (!optionalProject.isPresent())
            throw new NotFoundException("No such project for id: " + id);


        categoryService.getCategoryById(projectDTO.getProjectCategoryId());

        Project updatedProject = Project.builder()
                .id(id)
                .name(projectDTO.getName())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .projectCategoryId(projectDTO.getProjectCategoryId())
                .addedDate(LocalDateTime.now())
                .description(projectDTO.getDescription())
                .projectStatus(projectDTO.getProjectStatus())
                .build();


        projectRepo.save(updatedProject);
        return updatedProject;
    }

    public void deleteProject(Long id) {
        Optional<Project> optionalProject = projectRepo.findById(id);
        if (!optionalProject.isPresent())
            throw new NotFoundException("No such project for id: " + id);
        projectRepo.deleteById(id);
    }

    public void assignProject(ProjectUsersDTO projectUsersDTO) {
        ProjectUsersDTO projectUser  = getUnAssignedUsers(projectUsersDTO);
        List<ProjectMember> projectMembers = new ArrayList<>();

        for (Long userId : projectUser.getUserIds()) {
            ProjectMember projectMember = ProjectMember.builder()
                    .projectId(projectUser.getProjectId())
                    .userId(userId)
                    .addedDate(LocalDateTime.now())
                    .build();
            projectMembers.add(projectMember);
        }
        projectMembersRepo.saveAll(projectMembers);
    }

    public void unAssignProject(ProjectUsersDTO projectUser) {

        List<ProjectMember> projectMembers = new ArrayList<>();

        try {
            for (Long userId : projectUser.getUserIds()) {
                Optional<ProjectMember> projectMember = projectMembersRepo.findByUserIdAndProjectId(userId, projectUser.getProjectId());
                projectMembers.add(projectMember.get());
            }
            projectMembersRepo.deleteAll(projectMembers);

        } catch (Exception e) {
            throw new NotFoundException("No such members or project plz review your inputs");
        }
    }

    //Remove assigned users to the project
    private ProjectUsersDTO getUnAssignedUsers(ProjectUsersDTO projectUsers) {

       List<Long> projectUsersIds = projectUsers.getUserIds();
       List<ProjectMember> projectMembers = new ArrayList<>();
        for (Long userId : projectUsersIds) {
            Optional<ProjectMember> projectMember = projectMembersRepo.findByUserIdAndProjectId(userId, projectUsers.getProjectId());
            if (projectMember.isPresent())
                projectMembers.add(projectMember.get());
        }
//
//
//        Iterator<Long> projectUsersIds = projectUsers.getUserIds().listIterator();
//        while(projectUsersIds.hasNext()) {
//            Optional<ProjectMember> projectMember = projectMembersRepo.findByUserIdAndProjectId(projectUsersIds.next(), projectUsers.getProjectId());
//            if (projectMember.isPresent())
//                projectUsersIds.remove();
//
//        }
        projectUsers.setUserIds( projectUsersIds);
        return projectUsers;
    }
}
