package com.jeontongju.wishcart.dto.request;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartBuilder {
  public static Cart to(ConsumerCompositeKey key, String productId, Long amount) {
    return Cart.builder()
        .cartId(key)
        .productId(productId)
        .amount(amount)
        .build();
  }
}
