package com.jeontongju.wishcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.dto.request.CartBuilder;
import com.jeontongju.wishcart.repository.CartRepository;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import io.github.bitbox.bitbox.dto.CartDeleteDto;
import io.github.bitbox.bitbox.dto.CartDeleteListDto;
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

  @Autowired
  private KafkaConsumer kafkaConsumer;

  @Test
  void insertCart() {
    cartService.addProductToCart(-1L, "test-product-id", 20L);

    List<Cart> cartList = cartRepository.findByConsumerId(-1L);
    cartList.forEach(cart -> {
      assertEquals(cart.getProductId(), "test-product-id");
    });
  }

  @Test
  void deleteCartAmount() {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(-1L, "test-product-id");
    Cart cart = CartBuilder.to(cartId, 10L);
    cartRepository.save(cart);

    CartDeleteDto cartDeleteDto = CartDeleteDto.builder()
        .consumerId(-1L)
        .productId("test-product-id")
        .productCount(5L)
        .build();
    CartDeleteListDto request = CartDeleteListDto.builder().cartDeleteDtoList(List.of(cartDeleteDto)).build();
    kafkaConsumer.deleteCartListFromOrderService(request);
  }
}
