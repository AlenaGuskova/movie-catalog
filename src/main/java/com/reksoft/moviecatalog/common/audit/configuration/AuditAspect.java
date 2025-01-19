package com.reksoft.moviecatalog.common.audit.configuration;

import java.util.HashMap;
import java.util.Map;

import com.reksoft.moviecatalog.common.audit.annotation.AuditEvent;
import com.reksoft.moviecatalog.common.audit.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.expression.ThymeleafEvaluationContext;

/**
 * Aspect for audit.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private static final String ARGS_PARAMETER_NAME = "args";
    private static final String RESULT_PARAMETER_NAME = "result";
    private static final String EXCEPTION_PARAMETER_NAME = "exception";

    private final AuditEventRepository auditEventRepository;
    private final SpringTemplateEngine templateEngine;
    private final ThymeleafEvaluationContext thymeleafEvaluationContext;

    /**
     * Around advice for audit logging.
     *
     * @param joinPoint  {@link ProceedingJoinPoint}.
     * @param auditEvent {@link AuditEvent}.
     * @return result of method invocation.
     * @throws Throwable exceptions.
     */
    @Around("@annotation(auditEvent)")
    public Object logAround(ProceedingJoinPoint joinPoint,
                            AuditEvent auditEvent) throws Throwable {
        var eventName = auditEvent.name();
        var auditEntity = auditEventRepository.getByName(eventName);
        var args = joinPoint.getArgs();

        log.info(processStartMessage(auditEntity.getStartMessage(), args));
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.warn(processFailureMessage(auditEntity.getFailureMessage(),
                args, e));
            throw e;
        }
        log.info(processSuccessMessage(auditEntity.getSuccessMessage(),
            args, result));
        return result;
    }

    private String processStartMessage(String startMessage, Object[] args) {
        return processMessage(startMessage, Map.of(ARGS_PARAMETER_NAME, args));
    }

    private String processSuccessMessage(String successMessage, Object[] args,
                                         Object result) {
        var parameters = new HashMap<String, Object>();
        parameters.put(ARGS_PARAMETER_NAME, args);
        if (result != null) {
            parameters.put(RESULT_PARAMETER_NAME, result);
        }
        return processMessage(successMessage, parameters);
    }

    private String processFailureMessage(String failureMessage, Object[] args,
                                         Exception e) {
        return processMessage(failureMessage, Map.of(ARGS_PARAMETER_NAME, args,
            EXCEPTION_PARAMETER_NAME, e));
    }

    private String processMessage(String message,
                                  Map<String, Object> parameters) {
        var context = new Context();
        context.setVariable(
            ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME,
            thymeleafEvaluationContext);
        context.setVariables(parameters);
        return templateEngine.process(message, context);
    }
}
