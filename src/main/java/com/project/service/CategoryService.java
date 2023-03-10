package com.project.service;

import com.project.dto.PagingInfo;
import com.project.dto.ProjectCategoryDTO;
import com.project.entity.Project;
import com.project.entity.ProjectCategory;
import com.project.exception.NotFoundException;
import com.project.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public Page<ProjectCategory> getAllCategories(String searchTag, PagingInfo pagingInfo) {
        Page<ProjectCategory> projectCategories;
        Pageable pageable = PageRequest.of(pagingInfo.getPageNum() - 1, pagingInfo.getCount());

        if (searchTag != null && !searchTag.isEmpty())
            projectCategories = categoryRepo.findByNameContaining(searchTag, pageable);
        else
            projectCategories = categoryRepo.findAll(pageable);
        return projectCategories;
    }

    public ProjectCategory getCategoryById(Long id) {

        Optional<ProjectCategory> categoryOptional = categoryRepo.findById(id);
        if (!categoryOptional.isPresent())
            throw new NotFoundException("No such category for id: " + id);
        return categoryOptional.get();
    }

    public ProjectCategory addCategory(ProjectCategoryDTO projectCategoryDTO) {

        ProjectCategory projectCategory = ProjectCategory.builder()
                .Name(projectCategoryDTO.getName())
                .description(projectCategoryDTO.getDescription())
                .addedDate(LocalDateTime.now())
                .build();

        categoryRepo.save(projectCategory);
        log.info("Project category saved" + " id:" + projectCategory.getId());
        return projectCategory;
    }

    public ProjectCategory editCategory(Long id, ProjectCategoryDTO projectCategoryDTO) {

        Optional<ProjectCategory> optionalProjectCategory = categoryRepo.findById(id);
        if (!optionalProjectCategory.isPresent())
            throw new NotFoundException("No such category for id: " + id);

        ProjectCategory updatedProjectCategory = optionalProjectCategory.get();
        if (projectCategoryDTO.getName() != null)
            updatedProjectCategory.setName(projectCategoryDTO.getName());
        if (projectCategoryDTO.getDescription() != null)
            updatedProjectCategory.setDescription(projectCategoryDTO.getDescription());

        categoryRepo.save(updatedProjectCategory);
        log.info("Project category updated" + " id:" + id);
        return updatedProjectCategory;
    }


    public void deleteProjectCategory(Long id) {

        Optional<ProjectCategory> optionalProjectCategory = categoryRepo.findById(id);
        if (!optionalProjectCategory.isPresent())
            throw new NotFoundException("No such category for id: " + id);


        categoryRepo.deleteById(id);

    }
}
