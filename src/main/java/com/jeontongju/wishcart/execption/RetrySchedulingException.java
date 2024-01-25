package com.jeontongju.wishcart.execption;

public class RetrySchedulingException extends RuntimeException {
  private static final String message = "스케줄링 중 문제가 발생했습니다.";

  public RetrySchedulingException() {
    super(message);
  }

}
