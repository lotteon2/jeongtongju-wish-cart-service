package com.jeontongju.wishcart.controller;

import com.jeontongju.wishcart.service.WishService;
import io.github.bitbox.bitbox.dto.FeignFormat;
import io.github.bitbox.bitbox.dto.IsWishProductDto;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishFeignController {

  private final WishService wishService;

  @PostMapping("/products/likes")
  FeignFormat<HashMap<String, Boolean>> getIsWish(@RequestBody IsWishProductDto isWishProductDto) {
    return FeignFormat.<HashMap<String, Boolean>>builder()
        .code(HttpStatus.OK.value())
        .data(wishService.getIsWishProduct(isWishProductDto))
        .build();
  }

}
