package net.herit.platform.platform_spring.features.auth.dto.entity;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("AuthMngrEtt")
public class AuthMngrEtt {
    private int mngrId;
    private String pwd;
    private int fCnt;
    private String salt;
    private String ty;
    private String nm;
    private String eml;
    private Boolean svcYn;
    private String lgPth;
    private String lgNm;
}
