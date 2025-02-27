package net.herit.platform.platform_spring.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.herit.platform.platform_spring.common.util.HttpStatusRes;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiServiceException extends RuntimeException {
    private static final long serialVersionUID = 2L;
    private HttpStatusRes status;
    private Exception exception;
   
    public ApiServiceException(HttpStatusRes status, Exception e) {
        this.status = status;
        this.exception = e;
    }

    public ApiServiceException(HttpStatusRes status) {
        this.status = status;
        this.exception = new Exception(status.getMessage());
    }

}
