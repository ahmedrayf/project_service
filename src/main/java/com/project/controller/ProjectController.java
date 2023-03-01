package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.PagingInfo;
import com.project.dto.ProjectDTO;
import com.project.dto.response.CustomResponse;
import com.project.entity.Project;
import com.project.service.ProjectService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("getAll")
    public ResponseEntity getAllProjects(@RequestParam(name = "searchTag",required = false)  String searchTag,
                                         @RequestParam int pageNum ,
                                         @RequestParam int count){

        ObjectMapper mapper = new ObjectMapper();
        try{
            PagingInfo pagingInfo = PagingInfo.builder()
                    .pageNum(pageNum)
                    .count(count).build();

            Page<Project> result =  projectService.getAllProjects(searchTag , pagingInfo);
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .data(result)
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage("FAILED")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @GetMapping("{id}")
    public ResponseEntity getProjectById(@PathVariable Long id){
        try{
            Project result =  projectService.getProjectById(id);
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .data(result)
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
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
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .data(result)
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.CREATED
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PutMapping("update/{id}")
    public ResponseEntity editProject(@PathVariable Long id ,@RequestBody @Validated ProjectDTO projectDTO){
        try {
            Project result =  projectService.editProject(id , projectDTO);
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .data(result)
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
                    .HttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .HttpMessage(ex.getMessage())
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity deleteProject(@PathVariable Long id){
//        try {
//            Project result =  projectService.deleteProject(id);
//            return ResponseHandler.generateResponse("SUCCESSFULLY DELETED!", HttpStatus., result);
//        }
//        catch (Exception e){
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
//        }
//    }
//
//    @PostMapping("/assign")
//    public ResponseEntity assignProject(List<Project>,List<Users>){
//
//    }
//
//    @PostMapping("/unAssign")
//    public ResponseEntity unAssignProject(List<Project>,List<Users>){
//
//    }
//
//    @PostMapping("/approveOptIn/{projectId}/{statusId}")
//    public ResponseEntity approveOptInReq(
//            @PathVariable Long projectId ,@PathVariable int statusId, List<User>){
//
//    }
//
//    performOptOut(projectId, List<User>));
}
