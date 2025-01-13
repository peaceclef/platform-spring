package net.herit.platform.platform_spring.features.com.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonCodeReq {
    @NotEmpty
    private String cd;
    @NotEmpty
    @JsonProperty("prnt_cd")
    private String prntCd;
}
