package com.mb.module.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class AccountCreationDto {

    @JsonIgnore
    private Integer id;

    @NotNull(message = "Customer Id cannot be empty")
    @ApiModelProperty(notes = "Account owner Id", required = true)
    private Integer customerId;

    @NotNull
    @ApiModelProperty(notes = "Account owner country code", required = true)
    private String countryCode;

    @ApiModelProperty(notes = "List of currency is mandatory", required = true)
    @NotEmpty(message = "Currency cannot be empty.")
    private List<String> currencies;

}
