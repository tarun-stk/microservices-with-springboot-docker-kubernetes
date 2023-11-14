package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
public class ErrorResponseDto {


//    By default this ErrorResponseDto will not be showed inside swagger home page inside schemas
//    because this is not being returned at any method level in controller(api) class
//    this class will be only invoked from GlobalExceptionHandler class
//    so we have to implicitly mention this for each of the api call using content param inside @ApiResponse anno at method level

    @Schema(
            description = "API path invoked by client"
    )
//    for which api request uri error has occured.
    private String apiPath;


    @Schema(
            description = "Error code representing the error happened"
    )
//    corresponding error code;
    private HttpStatus errorCode;


    @Schema(
            description = "Error message representing the error happened"
    )
    private String errorMessage;

    @Schema(
            description = "Time representing when the error happened"
    )
    private LocalDateTime errorTime;
}
