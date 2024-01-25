package com.jeontongju.wishcart.kafka;

import static io.github.bitbox.bitbox.util.KafkaTopicNameInfo.DELETE_CART;
import static io.github.bitbox.bitbox.util.KafkaTopicNameInfo.DELETE_PRODUCT_TO_WISH_CART;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.dto.request.CartBuilder;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

  private final WishRepository wishRepository;

  private final CartRepository cartRepository;

  @Qualifier("redisGenericTemplate")
  private final RedisTemplate redisGenericTemplate;

  private final KafkaProcessor kafkaProcessor;

  @KafkaListener(topics = DELETE_PRODUCT_TO_WISH_CART)
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

  @KafkaListener(topics = DELETE_CART)
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

  @KafkaListener(id = "retry_listener_id", topics = "cart-add-retry", autoStartup = "false", groupId = "jeontongju")
  public void addCartRetry(ConsumerCompositeKey cartId) {
    log.info("retry product id : {}", cartId.getProductId());
    Long amount = 1L;
    if (cartRepository.existsById(cartId)) {
      amount += cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new)
          .getAmount();
    }

    Cart cart = CartBuilder.to(cartId, amount);
    cartRepository.save(cart);
  }

}
