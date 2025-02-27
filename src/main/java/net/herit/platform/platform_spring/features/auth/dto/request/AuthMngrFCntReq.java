package net.herit.platform.platform_spring.features.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthMngrFCntReq {
    private String lgnId;
    private int fCnt;
}
