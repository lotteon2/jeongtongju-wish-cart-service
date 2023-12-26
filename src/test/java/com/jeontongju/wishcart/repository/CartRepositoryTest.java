package com.jeontongju.wishcart.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.dto.request.CartBuilder;
import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
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
public class CartRepositoryTest {

  @Autowired
  private CartRepository cartRepository;

  @Test
  void insertCart() {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(-1L);
    Cart cart = CartBuilder.to(cartId, "test-product-id", 10L);
    cartRepository.save(cart);

    List<Cart> cartList = cartRepository.findByConsumerId(-1L);
    assertEquals(cartList.get(0).getProductId(), "test-product-id");
  }

}
