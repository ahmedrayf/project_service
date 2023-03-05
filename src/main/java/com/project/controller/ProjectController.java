package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.dto.ProjectUsersDTO;
import com.project.dto.response.AppResponse;
import com.project.entity.Project;
import com.project.service.ProjectService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("getAll")
    public ResponseEntity getAllProjects(@RequestParam(name = "searchTag", required = false) String searchTag,
                                         @RequestParam int pageNum,
                                         @RequestParam int count) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            PagingInfo pagingInfo = PagingInfo.builder()
                    .pageNum(pageNum)
                    .count(count).build();

            Page<Project> result = projectService.getAllProjects(searchTag, pagingInfo);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("projects", result))
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage("FAILED")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @GetMapping("{id}")
    public ResponseEntity getProjectById(@PathVariable @Min(1) Long id) {
        try {
            Project result = projectService.getProjectById(id);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("project", result))
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("save")
    public ResponseEntity addProject(@RequestBody @Validated ProjectDTO projectDTO) {
        try {
            Project result = projectService.addProject(projectDTO);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("project", result))
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.CREATED
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PutMapping("update/{id}")
    public ResponseEntity editProject(@PathVariable Long id, @RequestBody @Validated ProjectDTO projectDTO) {
        try {
            Project result = projectService.editProject(id, projectDTO);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("project", result))
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProjectCategory(@PathVariable @Min(1) Long id) {

        try {
            projectService.deleteProject(id);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/assign")
    public ResponseEntity assignProject(@RequestBody @Validated ProjectUsersDTO projectUser) {

        try {
            projectService.assignProject(projectUser);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("/unAssign")
    public ResponseEntity unAssignProject(@RequestBody @Validated ProjectUsersDTO projectUser) {

        try {
            projectService.unAssignProject(projectUser);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("/approveOptIn/{statusId}")
    public ResponseEntity approveOptInReq(@PathVariable int statusId, @RequestBody ProjectUsersDTO projectUser) {
        if (statusId != 1)
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.NOT_ACCEPTABLE)
                    .HttpMessage("Project opt in not approved")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.NOT_ACCEPTABLE
            );

        try {
            projectService.assignProject(projectUser);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("/approveOptOut/{statusId}")
    public ResponseEntity approveOptOutReq(@PathVariable int statusId, @RequestBody ProjectUsersDTO projectUser) {
        if (statusId != 2)
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.NOT_ACCEPTABLE)
                    .HttpMessage("Project opt Out not approved")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.NOT_ACCEPTABLE
            );

        try {
            projectService.unAssignProject(projectUser);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
}
