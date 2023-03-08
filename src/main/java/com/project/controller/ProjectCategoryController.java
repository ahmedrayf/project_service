package com.project.controller;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectCategoryDTO;
import com.project.dto.response.AppResponse;
import com.project.dto.response.PageableResponse;
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

    @Autowired
    private CategoryService categoryService;

    @GetMapping("getAll")
    public ResponseEntity<PageableResponse> getAllProjectsCategories(@RequestParam(required = false) String searchTag,
                                                           @RequestParam int pageNum,
                                                           @RequestParam int count) {
        try {
            PagingInfo pagingInfo = PagingInfo.builder()
                    .pageNum(pageNum)
                    .count(count).build();
            Page<ProjectCategory> result = categoryService.getAllCategories(searchTag, pagingInfo);
            return new ResponseEntity<>(PageableResponse.builder()
                    .body(result.getContent())
                    .httpStatus(HttpStatus.OK)
                    .totalItems(result.getTotalElements())
                    .currentItems(result.getNumberOfElements())
                    .currentPage(result.getPageable().getPageNumber() + 1)
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
    public ResponseEntity<AppResponse> getProjectCategoryById(@PathVariable @Min(1) Long id) {
        try {
            ProjectCategory result = categoryService.getCategoryById(id);
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
    public ResponseEntity<AppResponse> addProjectCategory(@RequestBody @Validated ProjectCategoryDTO projectCategoryDTO) {
        try {
            ProjectCategory result = categoryService.addCategory(projectCategoryDTO);
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
    public ResponseEntity<AppResponse> editProjectCategory(@PathVariable Long id, @RequestBody @Validated ProjectCategoryDTO projectCategoryDTO) {
        try {
            ProjectCategory result = categoryService.editCategory(id, projectCategoryDTO);
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
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteProjectCategory(@PathVariable @Min(1) Long id) {

        try {
            categoryService.deleteProjectCategory(id);
            return new ResponseEntity<>(AppResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Deleted Successfully")
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


}
