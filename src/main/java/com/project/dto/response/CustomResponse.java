package com.project.dto.response;


import com.project.entity.Project;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class CustomResponse extends BaseResponse {
    Object data;

}