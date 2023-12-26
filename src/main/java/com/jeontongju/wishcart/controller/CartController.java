package com.jeontongju.wishcart.controller;

import com.jeontongju.wishcart.client.ProductServiceFeignClient;
import com.jeontongju.wishcart.dto.response.ProductInfoAmountResponseDto;
import com.jeontongju.wishcart.dto.response.ProductInfoResponseDto;
import com.jeontongju.wishcart.execption.InvalidAmountException;
import com.jeontongju.wishcart.execption.StockOverException;
import com.jeontongju.wishcart.service.CartService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

  private final CartService cartService;

  private final ProductServiceFeignClient productClient;

  @GetMapping
  public ResponseEntity<ResponseFormat<Page<ProductInfoAmountResponseDto>>> getCartList(
      @RequestHeader Long memberId,
      @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Direction.DESC) Pageable pageable
  ) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<ProductInfoAmountResponseDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("장바구니 조회 성공")
                .data(cartService.getCartList(memberId, pageable))
                .build()
        );
  }

  @PostMapping("/{productId}")
  public ResponseEntity<ResponseFormat<Void>> insertCartList(
      @RequestHeader Long memberId, @PathVariable String productId,
      @RequestParam(required = false, defaultValue = "1") Long amount
  ) {
    if (amount < 0) throw new InvalidAmountException();

    List<Long> stocks = productClient.getProductStock(List.of(productId)).getData();
    if (stocks.get(0) < amount) throw new StockOverException(stocks.get(0));

    cartService.addProductToCart(memberId, productId, amount);

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("장바구니 추가 성공")
                .build()
        );
  }

  @PatchMapping("/{productId}")
  public ResponseEntity<ResponseFormat<Void>> modifyCart(
      @RequestHeader Long memberId, @PathVariable String productId,
      @RequestParam(required = false, defaultValue = "1") Long amount
  ) {
    if (amount < 0) throw new InvalidAmountException();

    List<Long> stocks = productClient.getProductStock(List.of(productId)).getData();
    if (stocks.get(0) < amount) throw new StockOverException(stocks.get(0));

    cartService.modifyProductInCart(memberId, productId, amount);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("장바구니 수정 성공")
                .build()
        );
  }

}
