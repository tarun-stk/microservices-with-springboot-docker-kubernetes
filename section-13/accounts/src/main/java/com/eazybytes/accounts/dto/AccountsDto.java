package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(  //open api related anno
        name = "Accounts",
        description = "Schema to hold Account information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDto {


//    Validation annos
//    when you're using this annos, before inserting data itself spring will check if the given data satisfies this conditions
//    These annos should be placed inside dtos only, because you're receiving dtos only in controller class.
//    To make these annos work, you should use @Valid anno along with @RequestBody in controller
//     @NotEmpty -> field must not be empty
//    @Size(min = 5, max = 30, message = "") -> must be of given limits, if not satisfied given message will be displayed
//    @Email -> checks for email format


//    when using example value inside @Schema anno, this value will appear inside schemas & also
//    for sample input inside api call request
//    when example is not used, then "string" will appear instead of some sample data,
//    example should be ignored if you have multiple possible values like positive and negative reponses
//    so instead of showing either positive or negative value, we can just ignore this to avoid confusion
    @Schema(
            description = "Account Number of Eazy Bank account", example = "3454433243"
    )
    @NotEmpty(message = "accountNumber cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})")
    private Long accountNumber;

    @Schema(
            description = "Account type of Eazy Bank account", example = "Savings"
    )
    @NotEmpty(message = "accountType cannot be empty")
    private String accountType;

    @Schema(
            description = "Eazy Bank branch address", example = "123 NewYork"
    )
    @NotEmpty(message = "branchAddress cannot be empty")
    private String branchAddress;

}
