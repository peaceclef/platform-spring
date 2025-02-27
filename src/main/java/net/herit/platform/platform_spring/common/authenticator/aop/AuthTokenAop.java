package net.herit.platform.platform_spring.common.authenticator.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import net.herit.platform.platform_spring.common.exception.ApiAopException;
import net.herit.platform.platform_spring.common.exception.ApiUtilException;
import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.call.CallLogger;
import net.herit.platform.platform_spring.common.system.ServiceInfo;
import net.herit.platform.platform_spring.common.util.HttpStatusRes;
import net.herit.platform.platform_spring.common.util.TokenCertUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthTokenAop {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    private TokenCertUtil tokenCertUtil;
    @Autowired
    private CallLogger clg;

    @Around("@annotation(net.herit.platform.platform_spring.common.authenticator.annotation.AuthToken)")
    public Object authTokenProd(ProceedingJoinPoint joinPoint) throws ApiAopException, ApiUtilException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(AUTHORIZATION_HEADER);

        clg.error(SourceToTarget.Non(ServiceInfo.name), () -> "AuthTokenAop : " + token);

        try {
            if(StringUtils.isEmpty(token)) {
                ApiAopException apiAopException = new ApiAopException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, new Exception("Token is empty"));
                request.setAttribute("exception", apiAopException);
                throw new ApiAopException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, new Exception("Token is empty"));
            }

            Map<String, Object> jwtPayload = tokenCertUtil.verifyAccessToken(token);
            request.setAttribute("token", jwtPayload);

            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new ApiAopException(HttpStatusRes.INTERNAL_SERVER_ERROR, e);
            }

        } catch (ApiAopException e) {
            throw new ApiAopException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, e);
        } catch (ApiUtilException e) {
            throw new ApiUtilException(e.getStatus(), e);
        } catch (Exception e) {
            throw new ApiAopException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, e);
        }
    }
}
