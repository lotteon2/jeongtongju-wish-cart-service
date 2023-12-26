package com.jeontongju.wishcart.dto.request;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import java.time.LocalDateTime;

public class CartBuilder {
  public static Cart to(ConsumerCompositeKey key, Long amount) {
    return Cart.builder()
        .cartId(key)
        .amount(amount)
        .created_at(LocalDateTime.now().toString())
        .build();
  }
}
