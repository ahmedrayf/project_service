package com.project.repo;

import com.project.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberStatusRepo extends JpaRepository<MemberStatus , Long> {

    Optional<MemberStatus> findByUserNameAndProjectId(String userName , Long projectId);

}
