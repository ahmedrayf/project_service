package com.project.controller;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectCategoryDTO;
import com.project.dto.response.AppResponse;
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
import java.util.Map;

@RestController
@RequestMapping("projectCategory")
public class ProjectCategoryController {

    @GetMapping("getAll")
    public ResponseEntity getAllProjects(@RequestParam(required = false)  String searchTag,
                                         @RequestParam int pageNum ,
                                         @RequestParam int count){
        try{
            PagingInfo pagingInfo = PagingInfo.builder()
                    .pageNum(pageNum)
                    .count(count).build();
            Page<ProjectCategory> result =  categoryService.getAllCategories(searchTag , pagingInfo);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("projectCategories",result))
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        }
        catch (Exception ex){
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

    @Autowired
    private CategoryService categoryService;

    @GetMapping("{id}")
    public ResponseEntity getProjectById(@PathVariable @Min(1) Long id){
        try{
            ProjectCategory result =  categoryService.getCategoryById(id);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("projectCategory",result))
                    .HttpStatusCode(HttpStatus.OK)
                    .HttpMessage("SUCCESS")
                    .HttpTimestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.OK
            );
        }
        catch (Exception ex){
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
    public ResponseEntity addProject(@RequestBody @Validated ProjectCategoryDTO projectCategoryDTO) {
        try {
            ProjectCategory result = categoryService.addCategory(projectCategoryDTO);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("projectCategory",result))
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
    public ResponseEntity editProject(@PathVariable Long id ,@RequestBody @Validated ProjectCategoryDTO projectCategoryDTO){
        try {
            ProjectCategory result =  categoryService.editCategory(id , projectCategoryDTO);
            return new ResponseEntity<>((AppResponse) AppResponse.builder()
                    .data(Map.of("projectCategory",result))
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
            categoryService.deleteProjectCategory(id);
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
