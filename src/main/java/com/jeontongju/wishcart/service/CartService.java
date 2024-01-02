package com.jeontongju.wishcart.service;

import com.jeontongju.wishcart.client.ProductServiceFeignClient;
import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.dto.request.CartBuilder;
import com.jeontongju.wishcart.dto.response.ProductInfoAmountResponseDto;
import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.repository.CartRepository;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import io.github.bitbox.bitbox.dto.ProductIdListDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;

  private final ProductServiceFeignClient productClient;

  public Page<ProductInfoAmountResponseDto> getCartList(Long consumerId, Pageable pageable) {
    int currentPage = pageable.getPageNumber();
    int pageSize = pageable.getPageSize();

    int startIndex = currentPage * pageSize;
    int endIndex = startIndex + pageSize;

    List<Cart> cartList = cartRepository.findByConsumerId(consumerId);
    if (cartList == null || cartList.isEmpty()) {
      return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }
    int totalSize = cartList.size();

    List<String> productList = cartList.stream()
        .sorted(Comparator.comparing(Cart::getCreated_at))
        .map(Cart::getProductId)
        .collect(Collectors.toList())
        .subList(startIndex, Math.min(endIndex, totalSize));

    Map<String, Long> amountMap = cartList.stream()
        .collect(Collectors.toMap(Cart::getProductId, Cart::getAmount));

    List<ProductInfoAmountResponseDto> result = productClient
        .getProductInfo(ProductIdListDto.builder().productIdList(productList).build())
        .getData()
        .stream()
        .map(productWishInfoDto -> ProductInfoAmountResponseDto
            .to(productWishInfoDto, amountMap.get(productWishInfoDto.getProductId()))
        )
        .collect(Collectors.toList());

    return new PageImpl<>(result, pageable, totalSize);
  }

  public void addProductToCart(Long consumerId, String productId, Long amount) {
    ConsumerCompositeKey cartId = ConsumerCompositeKey.of(consumerId, productId);
    if (cartRepository.existsById(cartId)) {
      amount += cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new).getAmount();
    }

    Cart cart = CartBuilder.to(cartId, amount);
    cartRepository.save(cart);
  }

  public void modifyProductInCart(Long consumerId, String productId, Long amount) {
    Cart cart = cartRepository.findById(ConsumerCompositeKey.of(consumerId, productId))
        .orElseThrow(CartNotFoundException::new);

    cartRepository.save(cart.toBuilder().amount(amount).build());
  }


  public void deleteProductInCart(Long consumerId, String productId) {
    ConsumerCompositeKey key = ConsumerCompositeKey.of(consumerId, productId);
    if (cartRepository.existsById(key)) {
      cartRepository.deleteById(key);
    } else {
      throw new CartNotFoundException();
    }
  }

  public void deleteConsumerCart(Long consumerId) {
    cartRepository.deleteAllByConsumerId(consumerId);
  }
}
