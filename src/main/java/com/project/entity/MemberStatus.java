package com.project.entity;

import com.project.enums.OptInStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    @NotNull
    private String userName;

    @Column(name = "project_id")
    @NotNull
    private Long projectId;

    @NotNull
    @Column(name = "opt_in_request")
    private int optInRequest = OptInStatus.PENDING.getValue();
}
