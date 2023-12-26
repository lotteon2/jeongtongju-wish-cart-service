package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.client.ProductServiceFeignClient;
import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.dto.response.ProductInfoResponseDto;
import com.jeontongju.wishcart.execption.PageExceededException;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import com.jeontongju.wishcart.repository.WishRepository;
import io.github.bitbox.bitbox.dto.ProductIdListDto;
import io.github.bitbox.bitbox.dto.ProductWishInfoDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishService {

  private final WishRepository wishRepository;

  @Qualifier("redisGenericTemplate")
  private final RedisTemplate redisGenericTemplate;

  private final ProductServiceFeignClient productClient;

  @CachePut(value = "wishList", key = "#consumerId")
  public Set<String> addDeleteWishItem(Long consumerId, String productId) {

    Set<String> wishSet = new HashSet<>();

    // 1. Redis에 존재할 경우
    if (redisGenericTemplate.hasKey(consumerId + "_wish_list")) {
      wishSet = redisGenericTemplate.opsForSet().members(consumerId + "_wish_list");
    } else {
      // 2. DynamoDB에 존재할 경우
      Optional<Wish> optionalWish = wishRepository.findById(consumerId);
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

  public Page<ProductInfoResponseDto> getWishList(Long consumerId, Pageable pageable) {
    int currentPage = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();

    int startIndex = currentPage * pageSize;
    int endIndex = startIndex + pageSize;
    Set<String> set = new HashSet<>();

    if (redisGenericTemplate.hasKey(consumerId + "_wish_list")) {
      set = redisGenericTemplate.opsForSet().members(consumerId + "_wish_list");
    } else if (wishRepository.existsById(consumerId)) {
      set = wishRepository.findById(consumerId)
          .orElseThrow(WishNotFoundException::new)
          .getProducts();
    }

    // 찜 목록이 비어있을 경우
    if (set == null || set.isEmpty()) {
      return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    int totalSize = set.size();
    if (totalSize < startIndex) {
      throw new PageExceededException();
    }

    List<String> list = set.stream()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList())
        .subList(startIndex, Math.min(endIndex, set.size()));

    List<ProductInfoResponseDto> result = productClient.getProductInfo(
            ProductIdListDto.builder()
                .productIdList(list)
                .build()
        )
        .getData()
        .stream()
        .map(ProductInfoResponseDto::to)
        .collect(Collectors.toList());

    return new PageImpl<>(result, pageable, totalSize);
  }

}
