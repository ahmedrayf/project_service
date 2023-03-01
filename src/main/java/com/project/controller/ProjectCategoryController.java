package com.project.controller;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectCategoryDTO;
import com.project.dto.ProjectDTO;
import com.project.dto.response.CustomResponse;
import com.project.entity.Project;
import com.project.entity.ProjectCategory;
import com.project.service.CategoryService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("projectCategory")
public class ProjectCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("getAll")
    public ResponseEntity getAllProjects(@RequestParam(name = "searchTag",required = false)  String searchTag,
                                         @RequestParam int pageNum ,
                                         @RequestParam int count){
        try{
            PagingInfo pagingInfo = PagingInfo.builder()
                    .pageNum(pageNum)
                    .count(count).build();
           Page<ProjectCategory> result =  categoryService.getAllCategories(searchTag , pagingInfo);
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
            ProjectCategory result =  categoryService.getCategoryById(id);
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
    public ResponseEntity addProject(@RequestBody ProjectCategoryDTO projectCategoryDTO) {
        try {
            ProjectCategory result = categoryService.addCategory(projectCategoryDTO);
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
    public ResponseEntity editProject(@PathVariable Long id ,@RequestBody @Validated ProjectCategoryDTO projectCategoryDTO){
        try {
            ProjectCategory result =  categoryService.editCategory(id , projectCategoryDTO);
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


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProjectCategory(@PathVariable @Min(1) Long id) {

        try {
            categoryService.deleteProjectCategory(id );
            return new ResponseEntity<>((CustomResponse) CustomResponse.builder()
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



}
