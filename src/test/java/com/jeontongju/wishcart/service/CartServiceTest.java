package com.jeontongju.wishcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.repository.CartRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartServiceTest {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartService cartService;

  @Test
  void insertCart() {
    cartService.addProductToCart(-1L, "test-product-id", 20L);

    List<Cart> cartList = cartRepository.findByConsumerId(-1L);
    cartList.forEach(cart -> {
      assertEquals(cart.getProductId(), "test-product-id");
    });
  }

}
