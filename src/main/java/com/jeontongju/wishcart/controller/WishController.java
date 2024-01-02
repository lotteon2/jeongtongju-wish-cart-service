package com.jeontongju.wishcart.controller;

import com.jeontongju.wishcart.dto.response.ProductInfoResponseDto;
import com.jeontongju.wishcart.service.WishService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

  private final WishService wishService;

  @GetMapping
  public ResponseEntity<ResponseFormat<Page<ProductInfoResponseDto>>> getWishList(
      @RequestHeader Long memberId,
      @PageableDefault(size = 10, page = 0, sort = "productId", direction = Direction.DESC) Pageable pageable
  ) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<ProductInfoResponseDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("찜 목록 조회 성공")
                .data(wishService.getWishList(memberId, pageable))
                .build()
        );
  }

  @PostMapping("/{productId}")
  public ResponseEntity<ResponseFormat<Void>> addDeleteWishItem(
      @RequestHeader Long memberId, @PathVariable String productId
  ) {
    wishService.addDeleteWishItem(memberId, productId);

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("찜 목록 추가/삭제 성공")
                .build()
        );
  }

  @DeleteMapping("/all")
  public ResponseEntity<ResponseFormat<Void>> deleteAllWishList(
      @RequestHeader Long memberId
  ) {
    wishService.deleteAllWishList(memberId);

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .detail("찜 목록 전체 삭제 성공")
                .build()
        );
  }

}
