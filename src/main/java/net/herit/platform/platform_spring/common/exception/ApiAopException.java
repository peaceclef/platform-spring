package net.herit.platform.platform_spring.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.herit.platform.platform_spring.common.util.HttpStatusRes;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiAopException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatusRes status;
    private Exception exception;
    private Throwable throwable;
   
    public ApiAopException(HttpStatusRes status, Exception e) {
        this.status = status;
        this.exception = e;
    }

    public ApiAopException(HttpStatusRes status, Throwable e) {
        this.status = status;
        this.throwable = e;
    }

    public ApiAopException(HttpStatusRes status) {
        this.status = status;
        this.exception = new Exception(status.getMessage());
    }

}
