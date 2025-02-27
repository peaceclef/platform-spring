package net.herit.platform.platform_spring.common.util;

import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import net.herit.platform.platform_spring.common.exception.ApiUtilException;

@Component
public class TokenCertUtil {
@Value("${token.access.secretkey}")
	private String ACCESS_SECRET_KEY;

	@Value("${token.refresh.secretkey}")
	private String REFRESH_SECRET_KEY;

	@Value("${token.issuer}")
	private String ISSUER;

	@Value("${token.access.expired.minute}")
	int accessExpiredMinute;

	@Value("${token.refresh.expired.minute}")
	int refreshExpiredMinute;

    private String createAccessToken(Map<String, Object> payload) {
        payload.put("sub", "1");

        return createToken(payload, ACCESS_SECRET_KEY, accessExpiredMinute);
    }

    private String createRefreshToken(Map<String, Object> payload) {
        payload.put("sub", "2");

        return createToken(payload, REFRESH_SECRET_KEY, refreshExpiredMinute);
    }

    private String createToken(Map<String, Object> payload, String secretKey, int expiredMinute) {
        Claims claims = Jwts.claims(payload);

        Date now = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, expiredMinute);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuer(ISSUER)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(cal.getTimeInMillis()))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public Map<String, Object> createCert(Map<String, Object> payload) {
        String accessToken = createAccessToken(payload);
        String refreshToken = createRefreshToken(payload);

        Map<String, Object> cert = new HashMap<>();
        cert.put("access_token", accessToken);
        cert.put("refresh_token", refreshToken);
        cert.put("mngr_id", payload.get("mngr_id"));
        cert.put("lgn_id", payload.get("lgn_id"));
        cert.put("type", payload.get("type"));
        cert.put("nm", payload.get("nm"));
        cert.put("eml", payload.get("eml"));
        cert.put("lg_pth", payload.get("lg_pth"));
        cert.put("lg_nm", payload.get("lg_nm"));

        return cert;
    }

    public Map<String, Object> verifyAccessToken(String token) throws ApiUtilException {
        try{
            Claims claims = decodeToken(token, ACCESS_SECRET_KEY);

            Map<String, Object> payload = new HashMap<>();
            payload.put("mngr_id", claims.get("mngr_id"));
            payload.put("lgn_id", claims.get("lgn_id"));
            payload.put("type", claims.get("type"));
            payload.put("nm", claims.get("nm"));
            payload.put("eml", claims.get("eml"));
            payload.put("lg_pth", claims.get("lg_pth"));
            payload.put("lg_nm", claims.get("lg_nm"));
            payload.put("sub", claims.get("sub"));

            Object exp = claims.get("exp");
            if(exp instanceof Long) {
                payload.put("exp", (Long) exp);
            } else if(exp instanceof Integer) {
                payload.put("exp", (Integer) exp);
            } else {
                payload.put("exp", (String) exp);
            }

            return payload;

        } catch (ExpiredJwtException e) {
            throw new ApiUtilException(HttpStatusRes.UNAUTHORIZED_TOKEN_EXPIRED, e);
        } catch (MalformedJwtException e) {
            throw new ApiUtilException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, e);
        } catch (Exception e) {
            throw new ApiUtilException(HttpStatusRes.UNAUTHORIZED_TOKEN_ERROR, e);
        }
    }

    private Claims decodeToken(String token, String secretKey) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }

    public Map<String, Object> verifyRefreshToken(String token) throws ApiUtilException {
        try{
            Claims claims = decodeToken(token, REFRESH_SECRET_KEY);

            Map<String, Object> payload = new HashMap<>();
            payload.put("mngr_id", claims.get("mngr_id"));
            payload.put("lgn_id", claims.get("lgn_id"));
            payload.put("type", claims.get("type"));
            payload.put("nm", claims.get("nm"));
            payload.put("eml", claims.get("eml"));
            payload.put("lg_pth", claims.get("lg_pth"));
            payload.put("lg_nm", claims.get("lg_nm"));
            payload.put("sub", claims.get("sub"));

            Object exp = claims.get("exp");
            if(exp instanceof Long) {
                payload.put("exp", (Long) exp);
            } else if(exp instanceof Integer) {
                payload.put("exp", (Integer) exp);
            } else {
                payload.put("exp", (String) exp);
            }

            return payload;

        } catch (ExpiredJwtException e) {
            throw new ApiUtilException(HttpStatusRes.UNAUTHORIZED_TOKEN_EXPIRED, e);
        } catch (MalformedJwtException e) {
            throw new ApiUtilException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, e);
        } catch (Exception e) {
            throw new ApiUtilException(HttpStatusRes.UNAUTHORIZED_TOKEN_ERROR, e);
        }
    }
}