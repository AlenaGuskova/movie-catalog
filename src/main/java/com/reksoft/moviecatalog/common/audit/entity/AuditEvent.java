package com.reksoft.moviecatalog.common.audit.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class AuditEvent {

    private UUID id;
    private String name;
    private String startMessage;
    private String successMessage;
    private String failureMessage;
}
