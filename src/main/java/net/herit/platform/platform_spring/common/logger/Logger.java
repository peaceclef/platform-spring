package net.herit.platform.platform_spring.common.logger;

import java.util.function.Supplier;

public class Logger {
    private final org.slf4j.Logger logger;
    private final Tracker tracker = new Tracker();
    
    public Logger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public void debug(SourceToTarget sourceToTarget, Supplier<Object> supplier) {
        debug(sourceToTarget, this.tracker, supplier);
    }

    public void debug(SourceToTarget sourceToTarget, String txId, int callId, Supplier<Object> supplier) {
        debug(sourceToTarget, new Tracker(txId, String.valueOf(callId)), supplier);
    }

    public void debug(SourceToTarget sourceToTarget, Tracker tracker, Supplier<Object> supplier) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(sourceToTarget.toString() + tracker.toString() + " " + supplier.get().toString());
        }
    }

    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    public void warn(SourceToTarget sourceToTarget, Supplier<Object> supplier) {
        warn(sourceToTarget, this.tracker, supplier);
    }

    public void warn(SourceToTarget sourceToTarget, String txId, int callId, Supplier<Object> supplier) {
        warn(sourceToTarget, new Tracker(txId, String.valueOf(callId)), supplier);
    }

    public void warn(SourceToTarget sourceToTarget, Tracker tracker, Supplier<Object> supplier) {
        if (this.logger.isWarnEnabled()) {
            this.logger.warn(sourceToTarget.toString() + tracker.toString() + " " + supplier.get().toString());
        }
    }

    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    public void info(SourceToTarget sourceToTarget, Supplier<Object> supplier) {
        info(sourceToTarget, this.tracker, supplier);
    }

    public void info(SourceToTarget sourceToTarget, String txId, int callId, Supplier<Object> supplier) {
        info(sourceToTarget, new Tracker(txId, String.valueOf(callId)), supplier);
    }

    public void info(SourceToTarget sourceToTarget, Tracker tracker, Supplier<Object> supplier) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(sourceToTarget.toString() + tracker.toString() + " " + supplier.get().toString());
        }
    }

    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    public void error(SourceToTarget sourceToTarget, Supplier<Object> supplier) {
        error(sourceToTarget, this.tracker, supplier);
    }

    public void error(SourceToTarget sourceToTarget, String txId, int callId, Supplier<Object> supplier) {
        error(sourceToTarget, new Tracker(txId, String.valueOf(callId)), supplier);
    }

    public void error(SourceToTarget sourceToTarget, Tracker tracker, Supplier<Object> supplier) {
        if (this.logger.isErrorEnabled()) {
            String prefix = sourceToTarget.toString() + tracker.toString() + " ";
            Object object = supplier.get();
            if (object instanceof Throwable) {
                Throwable throwable = printThrowableStackTrace(prefix, "", (Throwable) object);
                while (throwable != null) {
                    throwable = printThrowableStackTrace(prefix, "Caused by ", throwable);
                }
            } else {
                this.logger.error(prefix + " " + object.toString());
            }
        }
    }

    private Throwable printThrowableStackTrace(String tracker, String prefix, Throwable throwable) {
        this.logger.error(tracker + prefix + throwable);

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements != null) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                this.logger.error(tracker + "\tat " + stackTraceElement);
            }
        }

        return throwable.getCause();
    }
    
    public void data(SourceToTarget sourceToTarget, Supplier<Object> supplier) {
        data(sourceToTarget, this.tracker, supplier);
    }
    
    public void data(SourceToTarget sourceToTarget, Tracker tracker, Supplier<Object> supplier) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(supplier.get().toString());
        }
    }    
    
}
