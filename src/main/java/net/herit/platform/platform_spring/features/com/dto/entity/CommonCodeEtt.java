package net.herit.platform.platform_spring.features.com.dto.entity;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("commonCodeEtt")
public class CommonCodeEtt {
    private String cd;
    private String nm;
    private String prntCd;
    private String desc;
}
