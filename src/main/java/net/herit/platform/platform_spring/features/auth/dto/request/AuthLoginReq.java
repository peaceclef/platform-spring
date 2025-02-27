package net.herit.platform.platform_spring.features.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuthLoginReq {
   
    @JsonProperty("lgn_id")
    private String lgnId;
    
    private String pwd;
}
