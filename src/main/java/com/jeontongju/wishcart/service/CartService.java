package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.dto.request.CartBuilder;
import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.repository.CartRepository;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;

  public void addProductToCart(Long consumerId, String productId, Long amount) {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(consumerId, productId);
    if (cartRepository.existsById(cartId)) {
      amount += cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new).getAmount();
    }

    Cart cart = CartBuilder.to(cartId, amount);
    cartRepository.save(cart);
  }

  public void modifyProductInCart(Long consumerId, String productId, Long amount) {
    Cart cart = cartRepository.findById(ConsumerCompositeKey.of(consumerId, productId))
        .orElseThrow(CartNotFoundException::new);

    cartRepository.save(cart.toBuilder().amount(amount).build());
  }


  public void deleteProductInCart(Long consumerId, String productId) {
    ConsumerCompositeKey key = ConsumerCompositeKey.of(consumerId, productId);
    if (cartRepository.existsById(key)) {
      cartRepository.deleteById(key);
    } else {
      throw new CartNotFoundException();
    }
  }

  public void deleteConsumerCart(Long consumerId) {
    cartRepository.deleteAllByConsumerId(consumerId);
  }
}
