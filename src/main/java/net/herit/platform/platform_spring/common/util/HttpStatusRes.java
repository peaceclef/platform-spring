package net.herit.platform.platform_spring.common.util;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum HttpStatusRes {
    // 2xx Success
    OK(HttpStatus.OK, "200", "OK"),
    CREATED(HttpStatus.CREATED, "201", "Created"),
    ACCEPTED(HttpStatus.ACCEPTED, "202", "Accepted"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "204", "No Content"),

    // 3xx Redirection
    MOVED_PERMANENTLY(HttpStatus.MOVED_PERMANENTLY, "301", "Moved Permanently"),
    FOUND(HttpStatus.FOUND, "302", "Found"),
    SEE_OTHER(HttpStatus.SEE_OTHER, "303", "See Other"),
    NOT_MODIFIED(HttpStatus.NOT_MODIFIED, "304", "Not Modified"),
    TEMPORARY_REDIRECT(HttpStatus.TEMPORARY_REDIRECT, "307", "Temporary Redirect"),
    PERMANENT_REDIRECT(HttpStatus.PERMANENT_REDIRECT, "308", "Permanent Redirect"),

    // 4xx Client Error
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "Bad Request"),
    
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "400001", "Invalid Parameter"),
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "400002", "Invalid Parameter Type"),
    INVALID_PARAMETER_VALUE(HttpStatus.BAD_REQUEST, "400003", "Invalid Parameter Value"),
    INVALID_PARAMETER_LENGTH(HttpStatus.BAD_REQUEST, "400004", "Invalid Parameter Length"),
    INVALID_PARAMETER_FORMAT(HttpStatus.BAD_REQUEST, "400005", "Invalid Parameter Format"),
    INVALID_PARAMETER_DUPLICATE(HttpStatus.BAD_REQUEST, "400006", "Invalid Parameter Duplicate"),
    INVALID_PARAMETER_REQUIRED(HttpStatus.BAD_REQUEST, "400007", "Invalid Parameter Required"),
    INVALID_PARAMETER_NOT_FOUND(HttpStatus.BAD_REQUEST, "400008", "Invalid Parameter Not Found"),
    
    INVALID_BODY(HttpStatus.BAD_REQUEST, "400009", "Invalid Body"),
    INVALID_BODY_TYPE(HttpStatus.BAD_REQUEST, "400010", "Invalid Body Type"),
    INVALID_BODY_VALUE(HttpStatus.BAD_REQUEST, "400011", "Invalid Body Value"),
    INVALID_BODY_LENGTH(HttpStatus.BAD_REQUEST, "400012", "Invalid Body Length"),
    INVALID_BODY_FORMAT(HttpStatus.BAD_REQUEST, "400013", "Invalid Body Format"),
    INVALID_BODY_DUPLICATE(HttpStatus.BAD_REQUEST, "400014", "Invalid Body Duplicate"),
    INVALID_BODY_REQUIRED(HttpStatus.BAD_REQUEST, "400015", "Invalid Body Required"),
    INVALID_BODY_NOT_FOUND(HttpStatus.BAD_REQUEST, "400016", "Invalid Body Not Found"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "Unauthorized"),

    UNAUTHORIZED_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "401001", "Unauthorized Login Error"),
    UNAUTHORIZED_LOGIN_ID_ERROR(HttpStatus.UNAUTHORIZED, "401002", "Unauthorized Login ID Error"),
    UNAUTHORIZED_LOGIN_SVC_ERROR(HttpStatus.UNAUTHORIZED, "401003", "Unauthorized Login Service Error"),
    UNAUTHORIZED_EMAIL_ERROR(HttpStatus.UNAUTHORIZED, "401004", "Unauthorized Email Error"),
    UNAUTHORIZED_DVC_ID_ERROR(HttpStatus.UNAUTHORIZED, "401005", "Unauthorized Device ID Error"),
    UNAUTHORIZED_TAG_ID_ERROR(HttpStatus.UNAUTHORIZED, "401006", "Unauthorized Tag ID Error"),
    UNAUTHORIZED_LOGIN_BLOCKED(HttpStatus.UNAUTHORIZED, "401007", "Unauthorized Login Blocked"),
    UNAUTHORIZED_USER_PASSWORD(HttpStatus.UNAUTHORIZED, "401008", "Unauthorized User Password"),
    UNAUTHORIZED_LOGIN_ERROR_COUNT(HttpStatus.UNAUTHORIZED, "401009", "Unauthorized Login Error Count"),

    UNAUTHORIZED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "401010", "Unauthorized Token Error"),
    UNAUTHORIZED_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "401011", "Unauthorized Token Expired"),
    UNAUTHORIZED_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "401012", "Unauthorized Token Invalid"),
    UNAUTHORIZED_TOKEN_NOTEXIST(HttpStatus.UNAUTHORIZED, "401013", "Unauthorized Token Not Exist"),

    DUPLICATE_BLD_ID_ERROR(HttpStatus.BAD_REQUEST, "400014", "Duplicate Building ID Error"),
    DUPLICATE_BLD_NM_ERROR(HttpStatus.BAD_REQUEST, "400015", "Duplicate Building Name Error"),
    DUPLICATE_CHNL_ERROR(HttpStatus.BAD_REQUEST, "400016", "Duplicate Channel Error"),
    DUPLICATE_CTRT_NM_ERROR(HttpStatus.BAD_REQUEST, "400017", "Duplicate Contract Name Error"),
    DUPLICATE_EGW_ID_ERROR(HttpStatus.BAD_REQUEST, "400018", "Duplicate EGW ID Error"),
    
    FORBIDDEN(HttpStatus.FORBIDDEN, "403", "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Not Found"),

    NOT_FOUND_MANAGER(HttpStatus.NOT_FOUND, "404001", "Not Found Manager"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405", "Method Not Allowed"),
    CONFLICT(HttpStatus.CONFLICT, "409", "Conflict"),

    // 5xx Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Internal Server Error"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "503", "Service Unavailable"),

    SQL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "500001", "SQL Exception");

    private HttpStatus status;
    private String code;
    private String message;

    private HttpStatusRes(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus() {
        return status.value();
    }

    public String getHttpStatusCode(){
        return String.valueOf(status.value());
    }
}
