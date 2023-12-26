package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.dto.request.CartBuilder;
import com.jeontongju.wishcart.repository.CartRepository;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;

  public void addProductToCart(Long consumerId, String productId, Long amount) {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(consumerId);
    Cart cart = CartBuilder.to(cartId, productId, amount);
    cartRepository.save(cart);
  }

}
