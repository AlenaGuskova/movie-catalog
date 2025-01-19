package com.reksoft.moviecatalog.common.audit.repository;

import com.reksoft.moviecatalog.common.audit.entity.AuditEvent;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * {@inheritDoc}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcAuditEventRepository implements AuditEventRepository {

    private final NamedParameterJdbcOperations operations;
    private final RowMapper<AuditEvent> auditEventRowMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditEvent getByName(String name) {
        var query = """
            SELECT id, name, start_message, success_message, failure_message
            FROM audit_event
            WHERE name = :name
            """;
        return Try.of(() -> operations.queryForObject(query, Map.of("name", name), auditEventRowMapper))
                   .onSuccess(_ -> log.debug("Audit event for name:'{}' found successfully", name))
                   .onFailure(e -> log.warn("Exception occurred when finding audit event for name:'{}' from the database", name, e))
                   .getOrNull();
    }
}
