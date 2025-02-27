package net.herit.platform.platform_spring.features.auth.repository;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.herit.platform.platform_spring.features.auth.dto.entity.AuthMngrEtt;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthLoginReq;
import net.herit.platform.platform_spring.features.auth.dto.request.AuthMngrFCntReq;

@Mapper
public interface AuthRepository {

    AuthMngrEtt selectAuthMngrOne(AuthLoginReq dto) throws SQLException;

    int updateAuthMngrFCnt(AuthMngrFCntReq mngr) throws SQLException;

    void insertAndUpdateTokenCert(Map<String, Object> cert) throws SQLException;

    Map<String, Object> selectRefreshToken(Map<String, Object> jwtPayload) throws SQLException;

}
