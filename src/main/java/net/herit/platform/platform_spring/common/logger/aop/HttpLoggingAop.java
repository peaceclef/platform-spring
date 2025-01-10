package net.herit.platform.platform_spring.common.logger.aop;

import java.io.IOException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.call.CallLogger;

@Aspect
@Component
public class HttpLoggingAop {
    @Autowired
	private CallLogger clg;

    @Pointcut("execution(* net..*Controller.*(..))")
	private void httpLog() {};

    @Before("httpLog()")
	public void beforeHttpLog(JoinPoint joinPoint) throws IOException {
        

        clg.info(SourceToTarget.LeftIn("hzems", "http"), () -> "[result]" + joinPoint);
    }

    @AfterReturning(value = "httpLog()", returning = "returnObj")
	public void afterReturnHttpLog(JoinPoint joinPoint, Object returnObj) {
        clg.info(SourceToTarget.LeftOut("hzems", "http"), () -> "[result]" + joinPoint);
    }
}
