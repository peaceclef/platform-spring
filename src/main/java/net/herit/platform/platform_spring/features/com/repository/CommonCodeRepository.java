package net.herit.platform.platform_spring.features.com.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.herit.platform.platform_spring.features.com.dto.entity.CommonCodeEtt;
import net.herit.platform.platform_spring.features.com.dto.request.CommonCodeReq;

@Mapper
public interface CommonCodeRepository {

    public List<CommonCodeEtt> selectCommonCodeList();

    public List<CommonCodeEtt> selectCommonCodeList(CommonCodeReq dto);

}
