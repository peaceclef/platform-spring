package net.herit.platform.platform_spring.features.auth.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import net.herit.platform.platform_spring.common.exception.ApiServiceException;
import net.herit.platform.platform_spring.common.exception.ApiUtilException;
import net.herit.platform.platform_spring.common.util.HttpStatusRes;
import net.herit.platform.platform_spring.common.util.SecurityUtil;
import net.herit.platform.platform_spring.common.util.TokenCertUtil;
import net.herit.platform.platform_spring.features.auth.dto.entity.AuthMngrEtt;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthLoginReq;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthMngrFCntReq;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthTokenReq;
import net.herit.platform.platform_spring.features.auth.repository.AuthRepository;

@Service
public class AuthService {
    @Autowired
    private AuthRepository rpt;
    @Autowired
    private SecurityUtil secUtil;
    @Autowired
    private TokenCertUtil tokenUtil;

    public Map<String, Object> prodLogin(AuthLoginReq dto) throws ApiServiceException {

        try{
            // 1. 사용자 정보 조회
            AuthMngrEtt mngr = rpt.selectAuthMngrOne(dto);

            // 2. 사용자 정보가 없을 경우 예외 처리
            if(mngr == null) {
                throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_LOGIN_ERROR);
            }

            // 2-1. 서비스 사용 권한이 없는 경우 예외 처리
            if(!mngr.getSvcYn()){
                throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_LOGIN_SVC_ERROR);
            }
            
            // 2-2. 패스워드 5회 실패 일때 예외 처리
            if(mngr.getFCnt() >= 5){
                throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_LOGIN_ERROR_COUNT);
            }

            // 3. 비밀번호 일치 여부 확인
            boolean isMatch = secUtil.isPwdMatch(dto.getPwd(), mngr.getPwd(), mngr.getSalt());
            if(!isMatch) {
                mngr.setFCnt(mngr.getFCnt() + 1);
            } else {
                mngr.setFCnt(0);
            }
            
            // 4. 비밀번호 오류 횟수 업데이트
            rpt.updateAuthMngrFCnt(AuthMngrFCntReq.builder()
                .lgnId(dto.getLgnId())
                .fCnt(mngr.getFCnt())
                .build());

            // 5. 비밀번호 불일치 시 예외 처리
            if (!isMatch) {
                throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_LOGIN_ERROR_COUNT);
            }

            // 6. 로그인 성공 시 토큰 생성
            Map<String, Object> payload = new HashMap<String, Object>();
            payload.put("lgn_id", dto.getLgnId());
            payload.put("type", mngr.getTy());
            payload.put("nm", mngr.getNm());
            payload.put("eml", mngr.getEml());
            payload.put("mngr_id", mngr.getMngrId());
            Map<String, Object> cert = tokenUtil.createCert(payload);

            // 7. 로그인 성공 토큰저장 (등록 및 갱신)
            rpt.insertAndUpdateTokenCert(cert);
            
            cert.put("lg_pth", mngr.getLgPth());
            cert.put("lg_nm", mngr.getLgNm());

            return cert;
        } catch (SQLException e) {
            throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_LOGIN_ERROR);
        }
    }

    public Map<String, Object> generateToken(AuthTokenReq dto) throws ApiServiceException {
        try {
            Map<String, Object> jwtPayload = tokenUtil.verifyRefreshToken(dto.getRefreshToken());

            Map<String, Object> result = rpt.selectRefreshToken(jwtPayload);
            if(result == null) {
                throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_TOKEN_NOTEXIST);
            }

            Map<String, Object> cert = tokenUtil.createCert(jwtPayload);
            rpt.insertAndUpdateTokenCert(cert);
            return cert;
        } catch (ApiUtilException e) {
            throw new ApiServiceException(e.getStatus(), e);
        } catch (ExpiredJwtException e) {
            throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_TOKEN_EXPIRED, e);
        } catch (MalformedJwtException e) {
            throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID, e);    
        } catch (SQLException e) {
            throw new ApiServiceException(HttpStatusRes.SQL_EXCEPTION, e);
        } catch (Exception e) {
            throw new ApiServiceException(HttpStatusRes.UNAUTHORIZED_TOKEN_INVALID);
        }
    }
}
