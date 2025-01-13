package net.herit.platform.platform_spring.features.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.herit.platform.platform_spring.features.com.dto.entity.CommonCodeEtt;
import net.herit.platform.platform_spring.features.com.dto.request.CommonCodeReq;
import net.herit.platform.platform_spring.features.com.repository.CommonCodeRepository;

@Service
public class CommonCodeService {
    @Autowired
    private CommonCodeRepository rpt;

    public List<CommonCodeEtt> getCommonCodeList() {
        List<CommonCodeEtt> result = rpt.selectCommonCodeList();

        return result;
    }

    public List<CommonCodeEtt> getCommonCode(CommonCodeReq dto) {
        List<CommonCodeEtt> result = rpt.selectCommonCodeList(dto);

        return result;
    }

}
