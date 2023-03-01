package com.project.repo;

import com.project.entity.ProjectCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<ProjectCategory, Long> {
    Page<ProjectCategory> findByNameContaining(String searchTag, Pageable pageable);
}
