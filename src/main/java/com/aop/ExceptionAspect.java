package com.aop;

import com.common.exception.EntityNotFoundException;
import com.common.exception.ParamException;
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
 * description: ExceptionAspect <br>
 * date: 2019/9/23 16:57 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
@Aspect
@Component
public class ExceptionAspect {
    private final static Logger log = LoggerFactory.getLogger("SysLog");

    @Pointcut("execution(public * com.sxgis.hzcom.controller..*.*(..))" +
            "&& !execution(public * com.sxgis.hzcom.controller.base.*.*(..))")
    public void logException() {
    }

    @Before("logException()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("doBefore** " + joinPoint);
    }

    @After("logException()")
    public void doAfter(JoinPoint joinPoint) {
        log.debug("doAfter** " + joinPoint);
    }

    @AfterReturning("logException()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.debug("doAfterReturning** " + joinPoint);
    }

    @AfterThrowing(pointcut = "logException() && @annotation(apiOperation)", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, ApiOperation apiOperation, Throwable ex) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        TUser user = (TUser) request.getSession().getAttribute("session_user_info");
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
        String s = joinPoint.getSignature().getName();
        log.info("请求方法:{}; 方法描述:{}; 请求参数:{}; 请求人:{}; 请求IP:{}", new String[]{reqMethod, "", params,
                operator, ip});

        // 非系统错误不打印栈信息
        if (ex instanceof ParamException || ex instanceof EntityNotFoundException) {
            log.error(ex.getMessage());
            return;
        }
        log.error(params, ex);
    }

    @Around("logException()")
    public Object deAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("deAround** " + joinPoint);
        return joinPoint.proceed();
    }
}
