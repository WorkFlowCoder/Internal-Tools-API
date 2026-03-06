package com.techcorp.management.entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statut opérationnel")
public enum ToolStatus {
    active,
    deprecated,
    trial
}