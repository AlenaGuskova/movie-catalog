package com.reksoft.moviecatalog.common.audit.configuration;

import com.reksoft.moviecatalog.common.audit.entity.AuditEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.thymeleaf.spring6.expression.ThymeleafEvaluationContext;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * AutoConfiguration for audit
 */
@Configuration
public class AuditAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public StringTemplateResolver stringTemplateResolver() {
        return new StringTemplateResolver();
    }

    @ConditionalOnMissingBean
    @Bean
    public ThymeleafEvaluationContext context(ApplicationContext applicationContext) {
        return new ThymeleafEvaluationContext(applicationContext, null);
    }

    @Bean
    RowMapper<AuditEvent> auditEventRowMapper() {
        return new BeanPropertyRowMapper<>(AuditEvent.class);
    }
}
