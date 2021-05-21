package com.aop;

import com.common.utils.AopUtils;
import com.model.TUser;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * description: SendMailAspect <br>
 * date: 2019/2/27 16:58 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
@Aspect
@Component
public class SustainAspect {
    private final static Logger log = LoggerFactory.getLogger("ReqLog");

    //    @Pointcut("execution(public * com.sxgis.audit.controller.*.*(..))")
    @Pointcut("execution(public * com.sxgis.hzcom.controller..*.*(..))" +
            "&& !execution(public * com.sxgis.hzcom.controller.base.*.*(..))")
    public void logSustain() {
    }

    @Before("logSustain()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("doBefore**" + joinPoint);
    }

    @After("logSustain() && @annotation(apiOperation)")
    public void after(JoinPoint joinPoint, ApiOperation apiOperation) throws ClassNotFoundException {
        // 读取session中的用户
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        TUser user = (TUser) request.getSession().getAttribute("user");
        String operator = "未知用户(无Session)";
        if (user != null) {
            operator = user.getName();
        }
        String ip = request.getRemoteAddr();
        String params = AopUtils.genReqParams(joinPoint);
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String logContent = apiOperation.value();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String reqMethod = (joinPoint.getTarget().getClass().getName() + "."
                + joinPoint.getSignature().getName() + "()");
        log.info("请求方法:{}; 方法描述:{}; 请求参数:{}; 请求人:{}; 请求IP:{}", new String[]{reqMethod, logContent, params,
                operator, ip});
    }

    @AfterReturning("logSustain()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.debug("doAfterReturning**" + joinPoint);
    }

    @AfterThrowing(pointcut = "logSustain()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Exception {
        log.debug("deAfterThrowing**" + joinPoint);
    }

    @Around("logSustain()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("deAround**" + joinPoint);
        return joinPoint.proceed();
    }
}
