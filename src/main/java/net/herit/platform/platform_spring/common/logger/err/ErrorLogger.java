package net.herit.platform.platform_spring.common.logger.err;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ErrorLogger extends net.herit.platform.platform_spring.common.logger.Logger {

    public ErrorLogger() {
        super(log);
    }

}
