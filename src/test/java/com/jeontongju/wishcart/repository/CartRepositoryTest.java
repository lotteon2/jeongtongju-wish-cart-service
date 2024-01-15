package com.jeontongju.wishcart.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.dto.request.CartBuilder;
import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartRepositoryTest {

  @Autowired
  private CartRepository cartRepository;

  @Test
  @Order(1)
  void insertCart() {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(-1L, "test-product-id");
    Cart cart = CartBuilder.to(cartId, 10L);
    cartRepository.save(cart);

    List<Cart> cartList = cartRepository.findByConsumerId(-1L);
    assertEquals(cartList.get(0).getProductId(), "test-product-id");
  }

  @Test
  @Order(2)
  void getCartWithCompositeKey() {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(-1L, "test-product-id");
    Cart cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

    assertEquals(cart.getProductId(), "test-product-id");
  }

  @Test
  @Order(3)
  void deleteEmptyCart() {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(-1L, "test-product-id");
    cartRepository.deleteById(cartId);

    cartRepository.deleteAllByConsumerId(-1L);
  }

  @Test
  @Order(4)
  void removeCartAmount() {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(-1L, "test-product-id");
    Cart cart = CartBuilder.to(cartId, 10L);
    cartRepository.save(cart);

    Cart findCart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    cartRepository.save(cart.toBuilder().amount(5L).build());
//    cartRepository.deleteById(cartId);
  }

}
