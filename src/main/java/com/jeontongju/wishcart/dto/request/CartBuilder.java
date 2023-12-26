package com.jeontongju.wishcart.dto.request;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;

public class CartBuilder {
  public static Cart to(ConsumerCompositeKey key, String productId, Long amount) {
    return Cart.builder()
        .cartId(key)
        .productId(productId)
        .amount(amount)
        .build();
  }
}
