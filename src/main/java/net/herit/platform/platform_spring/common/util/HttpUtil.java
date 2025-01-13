package net.herit.platform.platform_spring.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class HttpUtil {

    public String builderString(InputStream input) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = input;
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return stringBuilder.toString().replaceAll("[\\t\\n\\r]+", "").replaceAll("    ", "");
	}
	
	public Map<String, Object> getReqHeader(HttpServletRequest request){
		Map<String, Object> reqHeader = new HashMap<>();
		Enumeration<String> requestHeaderNameList = request.getHeaderNames();
		while (requestHeaderNameList.hasMoreElements()) {
			String headerName = requestHeaderNameList.nextElement();
			reqHeader.put(headerName, request.getHeader(headerName));
		}
		return reqHeader;		
	}
	
	public RestTemplate getRestTemplate(int setConnectionRequestTimeout, int connectTimeout){
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(setConnectionRequestTimeout); // 요청 시간
        httpRequestFactory.setConnectTimeout(connectTimeout); // tcp 연결 시간
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
	}
}
