package com.brunomilford.picpaydesafiobackend.Authorization;

public record Authorization(String message) {

  public boolean isAuthorized() {
    return message.equals("Autorizado");
  }
}
