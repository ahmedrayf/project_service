package com.project.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AppResponse<T> {

    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime timestamp;
    private String exception;
    private Object validationErrors;
    private T  body;

}