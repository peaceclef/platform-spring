package net.herit.platform.platform_spring.common.logger.aop;

import java.io.IOException;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.Tracker;
import net.herit.platform.platform_spring.common.logger.call.CallLogger;
import net.herit.platform.platform_spring.common.system.ServiceInfo;
import net.herit.platform.platform_spring.common.util.CallFactory;
import net.herit.platform.platform_spring.common.util.HttpUtil;

@Aspect
@Component
public class HttpLoggingAop {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private HttpUtil util;
    @Autowired
    private CallLogger clg;

    @Pointcut("execution(* net..*Controller.*(..))")
    private void httpLog() {};

    @Before("httpLog()")
    public void beforeHttpLog(JoinPoint joinPoint) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String header_callId = null;
        String header_txId = null;
        Tracker tracker = null;
        
        Map<String, Object> header = util.getReqHeader(request);
        for(String key : header.keySet()) {
            if("callid".equals(key)) header_callId = (String) header.get(key);
            if("txid".equals(key)) header_txId = (String) header.get(key);
        }
        
        if(header_callId != null && header_txId != null) tracker = new Tracker(header_txId, header_callId);

        if(tracker == null) {
            int callId = CallFactory.newCallID();
            String txId = CallFactory.newTransactionID("REMS+", "HTTP", callId);
            tracker = new Tracker(txId, String.valueOf(callId));
            tracker.setJobId(joinPoint.getSignature().getName());
            request.setAttribute("tracker", tracker);
        } 

        // address 조회
        String address = request.getHeader("X-Forwarded-For");		    
        if (address == null) address = request.getRemoteAddr();

        // log 생성
        StringBuilder strReq = new StringBuilder();
        strReq.append(request.getMethod()).append(", ").append(request.getRequestURI()).append(", ")
        .append(request.getProtocol()).append(", ").append(address).append(", ").append(request.getServerPort()).append(", ");
        
        // params 조회
        String params = getRequestParams(joinPoint);
        
        if(params != null) {
            strReq.append("body=");
            strReq.append(params);
            strReq.append(", ");
        }
        strReq.append("header").append("=").append(header);
        
        clg.info(SourceToTarget.LeftIn(ServiceInfo.name, "HTTP"), tracker, () -> "req : " + strReq.toString());
    }

    @AfterReturning(value = "httpLog()", returning = "returnObj")
    public void afterReturnHttpLog(JoinPoint joinPoint, Object returnObj) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Tracker tracker = (Tracker) request.getAttribute("tracker");
        
        StringBuilder strParams = new StringBuilder();
        strParams.append(response.getStatus()).append(",");
        
        if(returnObj instanceof ResponseEntity) {
            ResponseEntity<?> resEntity = (ResponseEntity<?>) returnObj;
            strParams.append("body").append("=").append(resEntity.getBody());
        }
        
        clg.info(SourceToTarget.LeftOut(ServiceInfo.name, "HTTP"), tracker, () -> "res : " + strParams.toString());
    }

    private String getRequestParams(JoinPoint joinPoint) throws JsonProcessingException {
	    StringBuilder params = new StringBuilder();
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		String[] parameterNames = codeSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		for (int i = 0; i < parameterNames.length; i++) {
            String strObject = mapper.writeValueAsString(args[i]);
            params.append(strObject);
		}

        return params.toString();
	}
}
