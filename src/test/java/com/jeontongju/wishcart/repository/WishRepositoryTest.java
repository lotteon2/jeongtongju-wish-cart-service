package com.jeontongju.wishcart.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WishRepositoryTest {

  @Autowired
  private WishRepository wishRepository;

  @Autowired
  @Qualifier("redisGenericTemplate")
  private RedisTemplate redisGenericTemplate;

  @Test
  @Order(1)
  @DisplayName("찜목록 조회 테스트 - DynamoDB")
  void getWishListInDynamoDB() {
    Set<String> products = new HashSet<>();
    products.add("test1");
    products.add("test2");

    wishRepository.save(Wish.builder()
        .consumerId(-1L)
        .products(products)
        .build());

    Wish wish = wishRepository.findById(-1L).orElseThrow(WishNotFoundException::new);
    assertTrue(wish.getProducts().contains("test1"));

    Optional<Wish> optionalWish = wishRepository.findById(-2222L);
    assertThrows(WishNotFoundException.class, () -> {
      optionalWish.orElseThrow(WishNotFoundException::new);
    });
  }

  @Test
  @Order(2)
  @DisplayName("찜 목록 조회 테스트 - Reids")
  void getWishListInRedis() {
    redisGenericTemplate.opsForSet().add(-1L + "_wish_list", "test1", "test2");

    Set<String> wishList = redisGenericTemplate.opsForSet().members(-1L + "_wish_list");
    assertTrue(wishList.contains("test1"));
  }

}
