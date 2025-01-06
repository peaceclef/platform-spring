package net.herit.platform.platform_spring.features.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import net.herit.platform.platform_spring.features.com.dto.entity.CommonCodeEtt;
import net.herit.platform.platform_spring.features.com.dto.response.CommonCodeRes;
import net.herit.platform.platform_spring.features.com.service.CommonCodeService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class CommonCodeController {
    @Autowired
    private CommonCodeService srv;

    @GetMapping("/apis/v1/com/commonCode/list")
    public ResponseEntity<CommonCodeRes> getCommonCodeList(){

        List<CommonCodeEtt> list = srv.getCommonCodeList();

        CommonCodeRes result = CommonCodeRes.builder()
            .resultCode(HttpStatus.OK.value())
            .data(list)
            .build();

        return new ResponseEntity<CommonCodeRes>(result, HttpStatus.OK);
    }

}
