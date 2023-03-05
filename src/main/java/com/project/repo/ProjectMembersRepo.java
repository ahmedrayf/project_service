package com.project.repo;

import com.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMembersRepo extends JpaRepository<ProjectMember , Long> {

    Optional<ProjectMember> findByUserIdAndProjectId(Long userId , Long projectId);

}
