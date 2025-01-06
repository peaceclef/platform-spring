package net.herit.platform.platform_spring.features.com.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import net.herit.platform.platform_spring.features.com.dto.entity.CommonCodeEtt;

@Data
@Builder
public class CommonCodeRes {
    private int resultCode;
    private List<CommonCodeEtt> data;
}
