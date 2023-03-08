package com.project.repo;

import com.project.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusRepo extends JpaRepository<MemberStatus , Long> {

}
