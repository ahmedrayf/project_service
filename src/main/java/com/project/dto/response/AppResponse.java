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
public class AppResponse {

    private HttpStatus HttpStatusCode;
    private String HttpMessage;
    private LocalDateTime HttpTimestamp;
    private String HttpException;
    private Object InputErrors;
    private Map<?,?> data;

}