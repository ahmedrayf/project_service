package com.project.service;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectCategoryDTO;
import com.project.entity.Project;
import com.project.entity.ProjectCategory;
import com.project.repo.CategoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryService {

    public Page<ProjectCategory> getAllCategories(String searchTag, PagingInfo pagingInfo){
        Page<ProjectCategory> projectCategories;
        Pageable pageable = PageRequest.of(pagingInfo.getPageNum() - 1, pagingInfo.getCount());

        if (searchTag != null&& !searchTag.isEmpty() )
            projectCategories = categoryRepo.findByNameContaining(searchTag , pageable);
        else
            projectCategories = categoryRepo.findAll(pageable);
        return projectCategories;
    }

    @Autowired
    private CategoryRepo categoryRepo;

    public ProjectCategory getCategoryById(Long id){

        Optional<ProjectCategory> categoryOptional =  categoryRepo.findById(id);
        if (!categoryOptional.isPresent())
            throw new RuntimeException();
        return categoryOptional.get();
    }

    public ProjectCategory addCategory(ProjectCategoryDTO projectCategoryDTO) {

        ProjectCategory projectCategory = ProjectCategory.builder()
                .Name(projectCategoryDTO.getName())
                .description(projectCategoryDTO.getDescription())
                .projects(projectCategoryDTO.getProjects())
                .addedDate(LocalDateTime.now())
                .build();

       return categoryRepo.save(projectCategory);
    }

    public ProjectCategory editCategory(Long id , ProjectCategoryDTO projectCategoryDTO) {

        Optional<ProjectCategory> optionalProjectCategory = categoryRepo.findById(id);
        if (!optionalProjectCategory.isPresent())
            throw new RuntimeException();

        ProjectCategory projectCategory = ProjectCategory.builder()
                .id(id)
                .Name(projectCategoryDTO.getName())
                .description(projectCategoryDTO.getDescription())
                .projects(projectCategoryDTO.getProjects())
                .addedDate(LocalDateTime.now())
                .build();

        return categoryRepo.save(projectCategory);
    }


    public void deleteProjectCategory(Long id) {

        categoryRepo.deleteById(id);

    }
}
