package net.herit.platform.platform_spring.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmnResponse<T> {
    private int code;
    private T data;
}
