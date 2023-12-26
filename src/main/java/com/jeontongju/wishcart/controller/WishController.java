package com.jeontongju.wishcart.controller;

import com.jeontongju.wishcart.service.WishService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
