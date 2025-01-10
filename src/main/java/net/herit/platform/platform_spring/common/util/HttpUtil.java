package net.herit.platform.platform_spring.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpUtil {
	@Autowired(required=false)
	private HttpClient httpClient;

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
	
	public RestTemplate getRestTemplate(int readTimeout, int connectTimeout){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		//requestFactory.setHttpClient(this.httpClient);
		//requestFactory.setReadTimeout(readTimeout);
		requestFactory.setConnectionRequestTimeout(connectTimeout);		
		return new RestTemplate(requestFactory);
	}
}
