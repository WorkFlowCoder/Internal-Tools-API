package com.techcorp.management.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
    log.warn("BadRequestException : {}", message);
  }
}
