package com.brunomilford.picpaydesafiobackend.transaction;

public class InvalidTransacationException extends RuntimeException {
  public InvalidTransacationException(String message) {
    super(message);
  }
}