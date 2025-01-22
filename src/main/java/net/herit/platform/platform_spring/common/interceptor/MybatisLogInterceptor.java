package net.herit.platform.platform_spring.common.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.Tracker;
import net.herit.platform.platform_spring.common.logger.call.CallLogger;
import net.herit.platform.platform_spring.common.system.ServiceInfo;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class MybatisLogInterceptor implements Interceptor{
    @Autowired
    private CallLogger clg;

    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Tracker tracker = (Tracker) request.getAttribute("tracker");
        long start = System.currentTimeMillis();
        Object origin = invocation.proceed();

        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        Object paramObj = invocation.getArgs()[1];

        BoundSql boundSql = statement.getBoundSql(paramObj);
        StringBuilder paramSql = new StringBuilder(getParamBindSql(boundSql));
        paramSql.append(" ... " + (origin instanceof List ? ((List<?>) origin).size() : origin));
        paramSql.append(" ... " + (Clock.systemDefaultZone().millis() - start));

        clg.info(SourceToTarget.RightOut(ServiceInfo.name, "DB"), tracker, () -> "[SQL]" + paramSql.toString());
        
        return invocation.proceed();
    }

    public String getParamBindSql(BoundSql boundSql){
        Object parameterObject = boundSql.getParameterObject();
        StringBuilder sqlStringBuilder = new StringBuilder(sort(boundSql.getSql()));

        BiConsumer<StringBuilder, Object> sqlObjectReplace = (sqlSb, value) -> {
            int questionIdx = sqlSb.indexOf("?");

            if(questionIdx == -1){
                return;
            }

            if(value == null){
                sqlSb.replace(questionIdx, questionIdx + 1, "null");
            } else if(value instanceof String || value instanceof LocalDate || value instanceof LocalDateTime || value instanceof Enum<?>) {
                sqlSb.replace(questionIdx, questionIdx + 1, "'" + (value != null ? value.toString() : "") + "'");
            } else {
                sqlSb.replace(questionIdx, questionIdx + 1, value.toString() + "");
            }
        };

        if(parameterObject == null){
            sqlObjectReplace.accept(sqlStringBuilder, null);
        } else {
            if(parameterObject instanceof Integer || parameterObject instanceof Long || parameterObject instanceof Float || parameterObject instanceof Double || parameterObject instanceof String){
                sqlObjectReplace.accept(sqlStringBuilder, parameterObject);
            } else if(parameterObject instanceof Map){
                Map paramterObjectMap = (Map)parameterObject;
                List<ParameterMapping> paramMappings = boundSql.getParameterMappings();

                for (ParameterMapping parameterMapping : paramMappings) {
                    String propertyKey = parameterMapping.getProperty();
                    try {
                        Object paramValue = null;
                        if(boundSql.hasAdditionalParameter(propertyKey)) {
                            // 동적 SQL로 인해 __frch_item_0 같은 파라미터가 생성되어 적재됨, additionalParameter로 획득
                            paramValue = boundSql.getAdditionalParameter(propertyKey);
                        } else {
                            paramValue = paramterObjectMap.get(propertyKey);
                        }

                        sqlObjectReplace.accept(sqlStringBuilder, paramValue);
                    } catch (Exception e) {
                        sqlObjectReplace.accept(sqlStringBuilder, "[cannot binding : " + propertyKey+ "]");
                    }
                } 
            } else {
                List<ParameterMapping> paramMappings = boundSql.getParameterMappings();
                Class< ? extends Object> paramClass = parameterObject.getClass();

                for (ParameterMapping parameterMapping : paramMappings) {
                    String propertyKey = parameterMapping.getProperty();

                    try {
                        Object paramValue = null;
                        if(boundSql.hasAdditionalParameter(propertyKey)) {
                            // 동적 SQL로 인해 __frch_item_0 같은 파라미터가 생성되어 적재됨, additionalParameter로 획득
                            paramValue = boundSql.getAdditionalParameter(propertyKey);
                        } else {
                            Field field = ReflectionUtils.findField(paramClass, propertyKey);
                            field.setAccessible(true);
                            paramValue = field.get(parameterObject);
                        }

                        sqlObjectReplace.accept(sqlStringBuilder, paramValue);
                    } catch (Exception e) {
                        sqlObjectReplace.accept(sqlStringBuilder, "[cannot binding : " + propertyKey+ "]");
                    }
                }
            }
        }
        return sqlStringBuilder.toString().replaceAll("([\\r\\n\\s]){2,}([\\r\\n])+","\n");
    }

    private String sort(String sql) {
        return Stream.of(sql)
                .flatMap(mapper -> Stream.of(mapper.split("\n")))
                .map(mapper -> mapper.trim().replaceAll("\\s+", " "))
                .collect(Collectors.joining(" "));
    }
}
