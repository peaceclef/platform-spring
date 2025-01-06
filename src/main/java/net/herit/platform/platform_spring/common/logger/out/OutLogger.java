package net.herit.platform.platform_spring.common.logger.out;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.herit.platform.platform_spring.common.logger.Logger;
import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.Tracker;
import net.herit.platform.platform_spring.common.logger.err.ErrorLogger;

@Slf4j
@Component
public class OutLogger extends Logger {
    private final ErrorLogger elg;

    public OutLogger(ErrorLogger elg) {
        super(log);
        this.elg = elg;
    }

    @Override
    public void error(SourceToTarget sourceToTarget, Tracker tracker, Supplier<Object> supplier) {
        super.error(sourceToTarget, tracker, supplier);
        if(this.elg.isErrorEnabled()){
            this.elg.error(sourceToTarget, tracker, supplier);
        }
    }
}
