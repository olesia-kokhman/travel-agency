//package com.epam.finaltask.logging;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Arrays;
//
//@Component
//@Aspect
//@Slf4j
//public class LoggingAspect {
//
//    @Pointcut("execution(public * com.epam.finaltask.controller.*.*(..))")
//    public void controllerLog() {
//
//    }
//
//    @Pointcut("execution(public * com.epam.finaltask.service.*.*(..))")
//    public void serviceLog() {
//
//    }
//
//    @Before("controllerLog()")
//    public void doBeforeController(JoinPoint joinPoint) {
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest httpServletRequest = null;
//        if(servletRequestAttributes != null) {
//            log.info("NEW REQUEST: IP: {},\nURL: {},\nHTTP_METHOD: {},\nCONTROLLER_METHOD: {}.{}\n",
//                    httpServletRequest.getRemoteAddr(),
//                    httpServletRequest.getRequestURL().toString(),
//                    httpServletRequest.getMethod(),
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName());
//        }
//    }
//
//    @Before("serviceLog()")
//    public void doBeforeService(JoinPoint joinPoint) {
//        String className = joinPoint.getSignature().getDeclaringTypeName();
//        String methodName = joinPoint.getSignature().getName();
//
//        Object[] args = joinPoint.getArgs();
//        String argsString = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NO ARGUMENTS";
//
//        log.info("RUN SERVICE: SERVICE_METHOD: {}.{}\nMETHOD_ARGUMENTS: [{}]\n", className, methodName, argsString);
//    }
//
//    @AfterReturning(returning = "returnObject", pointcut = "controllerLog()")
//    public void doAfterReturning(Object returnObject) {
//        log.info("RETURN_VALUE: {}\n", returnObject);
//    }
//
//    @After("controllerLog()")
//    public void doAfter(JoinPoint joinPoint) {
//        log.info("Controller method executed successfully: {}.{}",
//                joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName());
//    }
//
//}
