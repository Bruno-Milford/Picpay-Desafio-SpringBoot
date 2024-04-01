package com.brunomilford.picpaydesafiobackend.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHanlder {

  @ExceptionHandler(InvalidTransacationException.class)
  public ResponseEntity<Object> handle(InvalidTransacationException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}