package com.project.repo;

import com.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
//    List<Project> findAllProjects(Pageable pageable);

//    Page<Project> findByProjectCategoryId( Long id, Pageable pageable);

    Page<Project> findByNameContaining(String name, Pageable pageable);
}
