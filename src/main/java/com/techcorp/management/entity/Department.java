package com.techcorp.management.entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Liste des départements internes")
public enum Department {
    Engineering,
    Sales,
    Marketing,
    HR,
    Finance,
    Operations,
    Design
}