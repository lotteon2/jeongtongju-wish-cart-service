package com.jeontongju.wishcart.execption;

import com.amazonaws.services.kms.model.NotFoundException;

public class CartNotFoundException extends NotFoundException {
  private static final String message = "장바구니 정보를 찾을 수 없습니다.";

  public CartNotFoundException() {
    super(message);
  }
}
