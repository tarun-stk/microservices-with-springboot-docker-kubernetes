package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

// changes name of schemas in swagger home page, giving them more meaningful names and description avoidign technical terms
@Schema(  //open api related anno
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
@Data
public class CustomerDto {

//    Validation annos
//    when you're using this annos, before inserting data itself spring will check if the given data satisfies these conditions
//    These annos should be placed inside dtos only, because you're receiving only dtos in controller class.
//    To make these annos work, you should use @Valid anno along with @RequestBody in controller
//     @NotEmpty -> field must not be empty
//    @Size(min = 5, max = 30, message = "") -> must be of given limits, if not satisfied given message will be displayed
//    @Email -> checks for email format

//    when not usign name param in @Schema anno, fieldname will be used
    @Schema(
            description = "Name of the customer", example = "Eazy Bytes"
    )
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "Name must be greater than 4 characters and less than 31 chars")
    private String name;

    @Schema(
            description = "Email address of the customer", example = "tutor@eazybytes.com"
    )
    @NotEmpty(message = "email cannot be empty")
    @Email(message = "email address should be a valid value")
    private String email;

    @Schema(
            description = "Mobile Number of the customer", example = "9345432123"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be of size 10 and can contain only digits")
    private String mobileNumber;

//    schema not required as we will descrieb then AccountsDto class itself
    private AccountsDto accountsDto;

}
