package com.example.jwtdemo.aspect;

import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.repositories.LogRepository;
import com.example.jwtdemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {}

    @Around(value = "executeLogging()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = generateTransactionId();
        log.info("Transaction started with ID: {}", transactionId);

        Throwable exception = null;
        Object returnValue = null;

        try {
            returnValue = joinPoint.proceed();
            log.info("Transaction completed with ID: {}", transactionId);
        } catch (Throwable e) {
            exception = e;
            log.error("Transaction failed with ID: {}", transactionId);
        }

        logMethodDetails(joinPoint, returnValue, exception);
        return returnValue;
    }

    private void logMethodDetails(ProceedingJoinPoint joinPoint, Object returnValue, Throwable exception) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        var message = buildMessage(joinPoint, returnValue);
        var timeStamp = LocalDateTime.now();
        var logLevel = (exception == null) ? LogLevel.INFO : LogLevel.ERROR;
        var stackTrace = (exception == null) ? null : Arrays.toString(exception.getStackTrace());
        var threadName = Thread.currentThread().getName();
        var className = joinPoint.getClass().getName();
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(ResourceNotFoundException::new);

        var userId = user.getId();

        var entry = LogEntry.builder()
                .timestamp(timeStamp)
                .loggerName(logRepository.getClass().getName())
                .logLevel(logLevel.toString())
                .threadName(threadName)
                .className(className)
                .methodName(methodName)
                .message(message)
                .exceptionStackTrace(stackTrace)
                .userId(userId)
                .build();

        logRepository.save(entry);

        if (exception != null) {
            throw exception;
        }
    }

    private String buildMessage(ProceedingJoinPoint joinPoint, Object returnValue) {
        StringBuilder message = new StringBuilder("Method: ");
        message.append(joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();

        if (null != args && args.length > 0) {
            message.append(" args=[ | ");
            Arrays.asList(args).forEach(arg -> message.append(arg).append(" | "));
            message.append("]");
        }

        if (returnValue instanceof Collection) {
            message.append(", returning: ").append(((Collection<?>) returnValue).size()).append(" instance(s)");
        } else {
            message.append(", returning: ").append(returnValue != null ? returnValue.toString() : null);
        }
        return message.toString();
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}

