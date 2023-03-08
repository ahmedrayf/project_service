package com.project.service;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.dto.ProjectUsersDTO;
import com.project.entity.Project;
import com.project.entity.ProjectMember;
import com.project.enums.ProjectStatus;
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
                .projectManagerId(projectDTO.getProjectManagerId())
                .description(projectDTO.getDescription())
                .projectStatus(projectDTO.getProjectStatus())
                .build();

        projectRepo.save(project);
        return project;
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
                .projectManagerId(projectDTO.getProjectManagerId())
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

        if (optionalProject.get().getProjectStatus() != ProjectStatus.DELIVERED.getValue())
            throw new RuntimeException("You can delete project in case only deleted");

        projectRepo.deleteById(id);
    }

    public void assignProject(ProjectUsersDTO projectUsersDTO) {
        ProjectUsersDTO projectUser  = getUnAssignedUsers(projectUsersDTO);
        List<ProjectMember> projectMembers = new ArrayList<>();

        for (String userName : projectUser.getUserNames()) {
            ProjectMember projectMember = ProjectMember.builder()
                    .projectId(projectUser.getProjectId())
                    .userName(userName)
                    .addedDate(LocalDateTime.now())
                    .build();
            projectMembers.add(projectMember);
        }
        projectMembersRepo.saveAll(projectMembers);
    }

    public void unAssignProject(ProjectUsersDTO projectUser) {

        List<ProjectMember> projectMembers = new ArrayList<>();

        try {
            for (String userName : projectUser.getUserNames()) {
                Optional<ProjectMember> projectMember = projectMembersRepo.findByUserNameAndProjectId(userName, projectUser.getProjectId());
                projectMembers.add(projectMember.get());
            }
            projectMembersRepo.deleteAll(projectMembers);

        } catch (Exception e) {
            throw new NotFoundException("No such members or project plz review your inputs");
        }
    }

    //Remove assigned users to the project
    private ProjectUsersDTO getUnAssignedUsers(ProjectUsersDTO projectUsers) {

       List<String> projectUserNames = projectUsers.getUserNames();
       List<ProjectMember> projectMembers = new ArrayList<>();
        for (String userName : projectUserNames) {
            Optional<ProjectMember> projectMember = projectMembersRepo.findByUserNameAndProjectId(userName, projectUsers.getProjectId());
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
        projectUsers.setUserNames(projectUserNames);
        return projectUsers;
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
}
