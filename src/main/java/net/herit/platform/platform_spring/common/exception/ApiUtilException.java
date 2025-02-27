package net.herit.platform.platform_spring.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.herit.platform.platform_spring.common.util.HttpStatusRes;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiUtilException extends RuntimeException {
    private static final long serialVersionUID = 3L;
    private HttpStatusRes status;
    private Exception exception;
   
    public ApiUtilException(HttpStatusRes status, Exception e) {
        this.status = status;
        this.exception = e;
    }

    public ApiUtilException(HttpStatusRes status) {
        this.status = status;
        this.exception = new Exception(status.getMessage());
    }

}
