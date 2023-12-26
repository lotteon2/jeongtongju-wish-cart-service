package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import com.jeontongju.wishcart.repository.WishRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishService {

  private final WishRepository wishRepository;

  @Qualifier("redisGenericTemplate")
  private final RedisTemplate redisGenericTemplate;

  @CachePut(value = "wishList", key = "#memberId")
  public Set<String> addDeleteWishItem(Long memberId, String productId) {

    Set<String> wishSet = new HashSet<>();

    // 1. Redis에 존재할 경우
    if (redisGenericTemplate.hasKey(memberId + "_wish_list")) {
      wishSet = redisGenericTemplate.opsForSet().members(memberId + "_wish_list");
    } else {
      // 2. DynamoDB에 존재할 경우
      Optional<Wish> optionalWish = wishRepository.findById(memberId);
      if (optionalWish.isPresent()) {
        wishSet = optionalWish.orElseThrow(WishNotFoundException::new).getProducts();
      }
    }

    if (wishSet.contains(productId)) {
      wishSet.remove(productId);
    } else {
      wishSet.add(productId);
    }

    return wishSet;
  }

  @Scheduled(cron = "0 0 */1 * * *")
  public void saveWishListInDynamo() {
    Set<String> keys = redisGenericTemplate.keys("*_wish_list");
    if (keys != null && !keys.isEmpty()) {
      keys.forEach(key -> {
        Long consumerId = Long.parseLong(key.replace("_wish_list", ""));
        Set<String> wishList = redisGenericTemplate.opsForSet().members(key);

        if (wishList.isEmpty() && wishRepository.existsById(consumerId)) {
          wishRepository.deleteById(consumerId);
        } else {
          wishRepository.save(
              Wish.builder()
                  .consumerId(consumerId)
                  .products(wishList)
                  .build()
          );
        }
      });
    }
  }
}
