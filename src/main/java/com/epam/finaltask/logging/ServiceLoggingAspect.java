package com.epam.finaltask.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class ServiceLoggingAspect {

    @Pointcut("within(com.epam.finaltask.service..*)")
    public void serviceLog() {}

    @Around("serviceLog()")
    public Object logServiceExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        Object[] args = proceedingJoinPoint.getArgs();
        String argsString = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NO ARGUMENTS";

        long start = System.nanoTime();
        log.info("SERVICE IN  {}.{} args={}", className, methodName, argsString);

        try {
            Object result = proceedingJoinPoint.proceed();
            long tookMs = (System.nanoTime() - start) / 1_000_000;

            log.info("SERVICE OUT {}.{} tookMs={} resultType={}",
                    className, methodName, tookMs,
                    (result == null ? "null" : result.getClass().getSimpleName())
            );
            return result;
        } catch (Throwable exception) {
            long tookMs = (System.nanoTime() - start) / 1_000_000;

            log.error("SERVICE ERR {}.{} tookMs={} exType={} msg={}",
                    className, methodName, tookMs,
                    exception.getClass().getSimpleName(),
                    exception.getMessage());
            throw exception;
        }
    }
}
