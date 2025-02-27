package net.herit.platform.platform_spring.features.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.herit.platform.platform_spring.common.dto.response.CmnResponse;
import net.herit.platform.platform_spring.common.exception.ApiServiceException;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthLoginReq;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthTokenReq;
import net.herit.platform.platform_spring.features.auth.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {
    @Autowired
    private AuthService srv;

    /**
     * [/apis/v1/auth/login] 로그인
     * @param dto
     * @return 
     * @throws ApiServiceException 
     */
    @PostMapping(path = "/apis/v1/auth/login")
    public ResponseEntity<CmnResponse<Map<String, Object>>> prodLogin(@Valid @RequestBody AuthLoginReq dto) throws ApiServiceException {
        
        Map<String, Object> cert = srv.prodLogin(dto);

        CmnResponse<Map<String, Object>> result = CmnResponse.<Map<String, Object>>builder()
            .code(HttpStatus.OK.value())
            .data(cert)
            .build();

        return new ResponseEntity<CmnResponse<Map<String, Object>>>(result, HttpStatus.OK);
    }

    @PostMapping(path = "/apis/v1/auth/token")
    public ResponseEntity<CmnResponse<Map<String, Object>>> generateToken(@RequestBody AuthTokenReq dto) throws ApiServiceException {
        
        Map<String, Object> cert = srv.generateToken(dto);

        CmnResponse<Map<String, Object>> result = CmnResponse.<Map<String, Object>>builder()
            .code(HttpStatus.OK.value())
            .data(cert)
            .build();

        return new ResponseEntity<CmnResponse<Map<String, Object>>>(result, HttpStatus.OK);
    }
}
