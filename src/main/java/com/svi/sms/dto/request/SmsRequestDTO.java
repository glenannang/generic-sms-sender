package com.svi.sms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SmsRequestDTO(
        @JsonProperty("telnum")
        @NotBlank(message = "Telephone number must not be blank.")
        @Pattern(
                regexp = "^09[0-9]{9}$",
                message = "Telephone number must start with 09 and contain exactly 11 digits."
        )
        String telnum,

        @JsonProperty("smscontent")
        @NotBlank(message = "SMS content must not be blank.")
        String smscontent
) {
}
