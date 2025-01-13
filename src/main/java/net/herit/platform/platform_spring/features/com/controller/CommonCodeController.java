package net.herit.platform.platform_spring.features.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.herit.platform.platform_spring.features.com.dto.entity.CommonCodeEtt;
import net.herit.platform.platform_spring.features.com.dto.request.CommonCodeReq;
import net.herit.platform.platform_spring.features.com.dto.response.CommonCodeRes;
import net.herit.platform.platform_spring.features.com.service.CommonCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CommonCodeController {
    @Autowired
    private CommonCodeService srv;

    @GetMapping("/apis/v1/com/commonCode/list")
    public ResponseEntity<CommonCodeRes> getCommonCodeList(){

        List<CommonCodeEtt> list = srv.getCommonCodeList();

        CommonCodeRes result = CommonCodeRes.builder()
            .code(HttpStatus.OK.value())
            .data(list)
            .build();

        return new ResponseEntity<CommonCodeRes>(result, HttpStatus.OK);
    }

    @PostMapping("/apis/v1/com/commonCode/")
    public ResponseEntity<CommonCodeRes> getCommonCode(@Valid @RequestBody CommonCodeReq dto){
        CommonCodeRes result = null;
        
        List<CommonCodeEtt> list = srv.getCommonCode(dto);

        result = CommonCodeRes.builder()
            .code(HttpStatus.OK.value())
            .data(list)
            .build();

        return new ResponseEntity<CommonCodeRes>(result, HttpStatus.OK);
    }

}
