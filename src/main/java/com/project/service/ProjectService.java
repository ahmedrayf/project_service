package com.project.service;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.dto.ProjectUsersDTO;
import com.project.entity.MemberStatus;
import com.project.entity.Project;
import com.project.entity.ProjectMember;
import com.project.enums.OptInStatus;
import com.project.enums.ProjectStatus;
import com.project.exception.NotFoundException;
import com.project.repo.MemberStatusRepo;
import com.project.repo.ProjectMembersRepo;
import com.project.repo.ProjectRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;
    private final CategoryService categoryService;
    private final ProjectMembersRepo projectMembersRepo;
    private final MemberStatusRepo memberStatusRepo;

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
        log.info("Project  saved" + " id:" + project.getId());
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

        if (!isProjectExist(projectUsersDTO.getProjectId()))
            throw new NotFoundException("No such Project");

        ProjectUsersDTO projectUser = getUnAssignedUsers(projectUsersDTO);
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

    public void optInReq(String userName, Long projectId) {

        try {
            if (!isProjectExist(projectId))
                throw new NotFoundException("No such Project");
            MemberStatus memberStatus = MemberStatus.builder()
                    .projectId(projectId)
                    .userName(userName)
                    .optInRequest(OptInStatus.PENDING.getKey())
                    .build();
            memberStatusRepo.save(memberStatus);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    //Remove assigned users to the project
    public void approveOptInReq(String userName, Long projectId) {
        try {
            Optional<MemberStatus> memberStatusOptional = memberStatusRepo.findByUserNameAndProjectId(userName, projectId);
            if (!memberStatusOptional.isPresent())
                throw new NotFoundException("No opt_in status for such data");

            MemberStatus memberStatus = memberStatusOptional.get();
            memberStatus.setOptInRequest(OptInStatus.ACCEPTED.getKey());
            memberStatusRepo.save(memberStatus);

            ProjectUsersDTO projectUser = ProjectUsersDTO.builder()
                    .projectId(projectId)
                    .userNames(List.of(userName))
                    .build();
            assignProject(projectUser);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

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

    private boolean isProjectExist(Long projectId){
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isPresent())
            return true;

        return false;
    }
}
