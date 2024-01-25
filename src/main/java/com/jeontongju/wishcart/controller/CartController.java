package com.jeontongju.wishcart.controller;

import com.jeontongju.wishcart.client.ProductServiceFeignClient;
import com.jeontongju.wishcart.dto.response.ProductInfoAmountResponseDto;
import com.jeontongju.wishcart.execption.InvalidAmountException;
import com.jeontongju.wishcart.kafka.KafkaProcessor;
import com.jeontongju.wishcart.service.CartService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @DeleteMapping("/{productId}")
  public ResponseEntity<ResponseFormat<Void>> deleteProductInCart(
      @RequestHeader Long memberId, @PathVariable String productId
  ) {

    cartService.deleteProductInCart(memberId, productId);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("장바구니 삭제 성공")
                .build()
        );
  }

  @DeleteMapping("/all")
  public ResponseEntity<ResponseFormat<Void>> deleteProductInCart(
      @RequestHeader Long memberId
  ) {

    cartService.deleteConsumerCart(memberId);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("장바구니 전체 삭제 성공")
                .build()
        );
  }

  @PostMapping("/start-retry")
  public ResponseEntity<ResponseFormat<Void>> startRetryTopicConsume() {

    cartService.startKafkaRetryTopicListen();
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("재시도 실행")
                .build()
        );
  }

  @PostMapping("/stop-retry")
  public ResponseEntity<ResponseFormat<Void>> stopRetryTopicConsume() {

    cartService.stopKafkaRetryTopicListen();
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("재시도 중단")
                .build()
        );
  }
}
