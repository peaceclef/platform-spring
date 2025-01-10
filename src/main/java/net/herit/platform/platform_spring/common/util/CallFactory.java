package net.herit.platform.platform_spring.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class CallFactory {
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMddHHmmss");
    private final static AtomicInteger FACTORY = new AtomicInteger(0);

    public static synchronized int newCallID() {
        int callId = FACTORY.incrementAndGet();
        if (callId < 0 || callId == 9999999 || callId > 9999999) {
            FACTORY.set(0);
        }
        return callId;
    }
    
    public static synchronized int currentCallID() {
    	return FACTORY.get();
    }

    public static String newTransactionID(String service, String source, int callId) {
        return "T." + service + "." + source + "." + LocalDateTime.now().format(DATE_TIME_FORMATTER) + String.format("%07d", callId);
    }
}
