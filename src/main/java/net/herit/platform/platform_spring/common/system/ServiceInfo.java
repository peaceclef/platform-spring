package net.herit.platform.platform_spring.common.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceInfo {
    public static String name;

    @Value("${spring.application.name}")
    public void setName(String appName){
        name = appName;
    }
}
