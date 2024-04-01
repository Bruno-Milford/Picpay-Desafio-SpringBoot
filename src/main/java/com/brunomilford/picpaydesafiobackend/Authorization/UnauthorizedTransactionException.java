package com.brunomilford.picpaydesafiobackend.Authorization;

public class UnauthorizedTransactionException extends RuntimeException {
 
  public UnauthorizedTransactionException(String message) {
    super(message);
  }
}