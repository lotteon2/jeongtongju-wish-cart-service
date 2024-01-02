package com.jeontongju.wishcart.execption;

public class InvalidAmountException extends RuntimeException {
  private static final String message = "재고는 음수일 수 없습니다.";

  public InvalidAmountException() {
    super(message);
  }
}
