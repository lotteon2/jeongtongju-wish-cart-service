package com.jeontongju.wishcart.execption;

public class PageExceededException extends RuntimeException {
  private static final String message = "조회하려는 페이지가 현재 페이지보다 많습니다.";

  public PageExceededException() {
    super(message);
  }
}
