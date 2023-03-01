package com.project.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class BaseResponse {

    private HttpStatus HttpStatusCode;
    private String HttpMessage;
    private LocalDateTime HttpTimestamp;
    private String HttpException;
    private Object InputErrors;

}
