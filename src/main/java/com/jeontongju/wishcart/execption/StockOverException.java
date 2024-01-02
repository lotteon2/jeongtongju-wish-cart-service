package com.jeontongju.wishcart.execption;

import lombok.Getter;

@Getter
public class StockOverException extends RuntimeException {
  private static final String message = "현재 재고 이상을 담을 수 없어요.";
  private long stock;

  public StockOverException(long stock) {
    super(message);
    this.stock = stock;
  }
}
