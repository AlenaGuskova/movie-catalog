package com.reksoft.moviecatalog.common.audit.repository;

import com.reksoft.moviecatalog.common.audit.entity.AuditEvent;
import com.reksoft.moviecatalog.domain.exception.DataAccessException;
import com.reksoft.moviecatalog.domain.exception.ResourceNotFoundException;

public interface AuditEventRepository {

    /**
     * Finds an audit event by its name.
     *
     * @param name audit event name.
     * @return found audit event.
     * @throws DataAccessException       when dao access exception happens.
     * @throws ResourceNotFoundException when audit event was not found by its identifier.
     */
    AuditEvent getByName(String name);
}

