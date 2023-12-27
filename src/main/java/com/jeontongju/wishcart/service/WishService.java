package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.client.ProductServiceFeignClient;
import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.dto.response.ProductInfoResponseDto;
import com.jeontongju.wishcart.execption.PageExceededException;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import com.jeontongju.wishcart.repository.WishRepository;
import io.github.bitbox.bitbox.dto.IsWishProductDto;
import io.github.bitbox.bitbox.dto.ProductIdListDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
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

  @CachePut(value = "wish_list", key = "#consumerId")
  public Set<String> addDeleteWishItem(Long consumerId, String productId) {

    Set<String> wishSet = getConsumersWishList(consumerId);

    if (wishSet.contains(productId)) {
      wishSet.remove(productId);
    } else {
      wishSet.add(productId);
    }

    return wishSet;
  }

  @Scheduled(cron = "0 0 */1 * * *")
  public void saveWishListInDynamo() {
    Set<String> keys = redisGenericTemplate.keys("wish_list::*");
    if (keys != null && !keys.isEmpty()) {
      keys.forEach(key -> {
        Long consumerId = Long.parseLong(key.replace("wish_list::", ""));
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
    Set<String> set = getConsumersWishList(consumerId);

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
        .subList(startIndex, Math.min(endIndex, totalSize));

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

  @CacheEvict(value = "wish_list", key = "#consumerId")
  public void deleteAllWishList(Long consumerId) {
    if (wishRepository.existsById(consumerId)) {
      wishRepository.deleteById(consumerId);
    }
  }

  public HashMap<String, Boolean> getIsWishProduct(IsWishProductDto request) {
    Set<String> set = getConsumersWishList(request.getConsumerId());
    HashMap<String, Boolean> map = new HashMap<>();

    request.getProductIds().forEach(productId -> {
      map.put(productId, set.contains(productId));
    });

    return map;
  }


  private Set<String> getConsumersWishList(Long consumerId) {
    Set<String> set = new HashSet<>();
    if (redisGenericTemplate.hasKey("wish_list::" + consumerId)) {
      set = (HashSet<String>) redisGenericTemplate.opsForValue().get("wish_list::" + consumerId);
    } else if (wishRepository.existsById(consumerId)) {
      set = wishRepository.findById(consumerId)
          .orElseThrow(WishNotFoundException::new)
          .getProducts();
    }
    return set;
  }
}
