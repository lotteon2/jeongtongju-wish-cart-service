package com.jeontongju.wishcart.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.repository.WishRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
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
public class WishServiceTest {

  @Autowired
  private WishRepository wishRepository;

  @Autowired
  @Qualifier("redisGenericTemplate")
  private RedisTemplate redisGenericTemplate;

  @Autowired
  private WishService wishService;

  @Test
  @DisplayName("찜목록 추가-삭제 캐싱 테스트")
  void insertDeleteAllWishList() {
    Set<String> products = new HashSet<>();
    products.add("test1");
    products.add("test2");

    wishRepository.save(Wish.builder()
        .consumerId(-1L)
        .products(products)
        .build());

    wishService.addDeleteWishItem(-1L, "test1");
    wishService.addDeleteWishItem(-1L, "test2");

    wishService.deleteAllWishList(-1L);

    assertFalse(wishRepository.existsById(-1L));
    assertFalse(redisGenericTemplate.hasKey("wish_list::" + -1L));
  }

}
