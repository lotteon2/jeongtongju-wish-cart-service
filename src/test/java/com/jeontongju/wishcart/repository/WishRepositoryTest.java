package com.jeontongju.wishcart.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
public class WishRepositoryTest {

  @Autowired
  private WishRepository wishRepository;

  @Test
  @Order(1)
  void getWishList() {
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

    Set<String> testSet = new HashSet<>();
    if (optionalWish.isPresent()) {
      testSet = optionalWish.get().getProducts();
      log.info("is present");
    } else {
      log.info("is not present");
    }
  }

}
