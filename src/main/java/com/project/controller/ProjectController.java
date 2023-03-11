package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.dto.ProjectUsersDTO;
import com.project.dto.response.AppResponse;
import com.project.dto.response.PageableResponse;
import com.project.entity.Project;
import com.project.enums.OptInStatus;
import com.project.service.ProjectService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("project")
@RequiredArgsConstructor
public class ProjectController {
    private final  ProjectService projectService;

    @GetMapping("getAll")
    public ResponseEntity<PageableResponse> getAllProjects(@RequestParam(name = "searchTag", required = false) String searchTag,
                                         @RequestParam int pageNum,
                                         @RequestParam int count) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            PagingInfo pagingInfo = PagingInfo.builder()
                    .pageNum(pageNum)
                    .count(count).build();

            Page<Project> result = projectService.getAllProjects(searchTag, pagingInfo);
            return new ResponseEntity<>(PageableResponse.builder()
                    .body(result.getContent())
                    .httpStatus(HttpStatus.OK)
                    .totalItems(result.getTotalElements())
                    .currentItems(result.getNumberOfElements())
                    .currentPage(result.getPageable().getPageNumber() +1)
                    .totalPages(result.getTotalPages())
                    .message("SUCCESS")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(PageableResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("FAILED")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<AppResponse> getProjectById(@PathVariable @Min(1) Long id) {
        try {
            Project result = projectService.getProjectById(id);
            return new ResponseEntity<>(AppResponse.builder()
                    .body(result)
                    .httpStatus(HttpStatus.OK)
                    .message("SUCCESS")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("save")
    public ResponseEntity<AppResponse> addProject(@RequestBody @Validated ProjectDTO projectDTO) {
        try {
            Project result = projectService.addProject(projectDTO);
            return new ResponseEntity<>(AppResponse.builder()
                    .body(result)
                    .httpStatus(HttpStatus.OK)
                    .message("Created Successfully")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.CREATED
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PutMapping("update/{id}")
    public ResponseEntity<AppResponse> editProject(@PathVariable Long id, @RequestBody @Validated ProjectDTO projectDTO) {
        try {
            Project result = projectService.editProject(id, projectDTO);
            return new ResponseEntity<>(AppResponse.builder()
                    .body(result)
                    .httpStatus(HttpStatus.OK)
                    .message("Updated Successfully")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProjectCategory(@PathVariable @Min(1) Long id) {

        try {
            projectService.deleteProject(id);
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Deleted")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/assign")
    public ResponseEntity assignProject(@RequestBody @Validated ProjectUsersDTO projectUser) {

        try {
            projectService.assignProject(projectUser);
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Assigned Successfully")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PutMapping("/unAssign")
    public ResponseEntity unAssignProject(@RequestBody @Validated ProjectUsersDTO projectUser) {

        try {
            projectService.unAssignProject(projectUser);
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Unassigned Successfully")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("optIn/{userName}/{projectId}")
    public ResponseEntity<AppResponse> optInReq(@PathVariable @NotBlank String userName ,@Min(1) @PathVariable Long projectId){

        try {
            projectService.optInReq(userName , projectId);
            return new ResponseEntity<>(AppResponse.builder()
                    .body(OptInStatus.PENDING.getValue())
                    .httpStatus(HttpStatus.OK)
                    .message("SUCCESS")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }



    }

    @PostMapping("approveOptIn/{userName}/{projectId}")
    public ResponseEntity<AppResponse> approveOptInReq(@PathVariable @NotBlank String userName , @PathVariable @Min(1) Long projectId) {

        try {
            projectService.approveOptInReq(userName,projectId);
            return new ResponseEntity<>(AppResponse.builder()
                    .body(OptInStatus.ACCEPTED.getValue())
                    .httpStatus(HttpStatus.OK)
                    .message("SUCCESS")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("/approveOptOut/{userName}/{projectId}")
    public ResponseEntity approveOptOutReq(@PathVariable String userName , @PathVariable Long projectId) {

        ProjectUsersDTO projectUser = ProjectUsersDTO.builder()
                .projectId(projectId)
                .userNames(List.of(userName))
                .build();
        try {
            projectService.unAssignProject(projectUser);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("SUCCESS")
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
}
