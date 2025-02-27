package net.herit.platform.platform_spring.features.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthTokenReq {
    @NotEmpty
    @JsonProperty("refresh_token")
    private String refreshToken;
}
