package net.herit.platform.platform_spring.common.logger.aop;

import org.apache.ibatis.binding.MapperProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.call.CallLogger;
import net.herit.platform.platform_spring.common.system.ServiceInfo;

@Aspect
@Component
public class MybatisLoggingAop {
    @Autowired
    private CallLogger clg;
    @Autowired
    private ObjectMapper mapper;

    @Pointcut("execution(* net..*Repository.*(..))")
    private void mybatisLog() {};

    @AfterReturning(value = "mybatisLog()", returning = "returnObj")
    public void afterReturnHttpLog(JoinPoint joinPoint, Object returnObj) throws JsonProcessingException {
        String returnStr = mapper.writeValueAsString(returnObj);
        clg.info(SourceToTarget.RightIn(ServiceInfo.name, "DB"), () -> "[result]" + returnStr);
    }
}
