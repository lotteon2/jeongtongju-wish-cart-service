package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.repository.CartRepository;
import com.jeontongju.wishcart.repository.WishRepository;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import io.github.bitbox.bitbox.dto.CartDeleteDto;
import io.github.bitbox.bitbox.dto.CartDeleteListDto;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class KafkaConsumer {

  private final WishRepository wishRepository;

  private final CartRepository cartRepository;

  @Qualifier("redisGenericTemplate")
  private final RedisTemplate redisGenericTemplate;

  @KafkaListener(topics = KafkaTopicNameInfo.DELETE_PRODUCT_TO_WISH_CART)
  public void deleteWishListFromProductService(List<String> productIds) {
    // in Redis
    Set<String> keys = redisGenericTemplate.keys("wish_list::*");

    if (keys != null & !keys.isEmpty()) {
      keys.forEach(key -> {
        Set<String> wishList = redisGenericTemplate.opsForSet().members(key);

        productIds.forEach(productId -> wishList.remove(productId));

        redisGenericTemplate.opsForValue().set(key, wishList);
      });
    }

    // in DynamoDB
    Iterable<Wish> wishIterable = wishRepository.findAll();

    wishIterable.forEach(wish -> {
      productIds.forEach(productId -> wish.getProducts().remove(productId));
      wishRepository.save(wish);
    });
  }

  @KafkaListener(topics = KafkaTopicNameInfo.DELETE_CART)
  public void deleteCartListFromOrderService(CartDeleteListDto request) {
    List<CartDeleteDto> cartDeleteDtoList = request.getCartDeleteDtoList();
    Long consumerId = cartDeleteDtoList.get(0).getConsumerId();

    cartDeleteDtoList.forEach(cartDeleteDto -> {
      ConsumerCompositeKey key = ConsumerCompositeKey.of(consumerId, cartDeleteDto.getProductId());
      Cart cart = cartRepository.findById(key).orElseThrow(CartNotFoundException::new);
      Long amount = cart.getAmount() - cartDeleteDto.getProductCount();

      if (amount <= 0) {
        cartRepository.deleteById(key);
      } else {
        cartRepository.save(cart.toBuilder().amount(amount).build());
      }
    });
  }

}
