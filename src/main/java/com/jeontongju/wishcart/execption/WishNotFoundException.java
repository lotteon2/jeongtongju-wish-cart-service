package com.jeontongju.wishcart.execption;

import com.amazonaws.services.kms.model.NotFoundException;

public class WishNotFoundException extends NotFoundException {
  private static final String message = "찜 정보를 찾을 수 없습니다.";

  public WishNotFoundException() {
    super(message);
  }
}
