package net.herit.platform.platform_spring.common.util;

import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public boolean isPwdMatch(String pwd, String encPwd, String salt) {
        SHA256 sha256Pwd = new SHA256((pwd + salt).getBytes());
        String enc = Base64.getEncoder().encodeToString(sha256Pwd.GetHashCode());
        return encPwd.equals(enc);
    }
}
