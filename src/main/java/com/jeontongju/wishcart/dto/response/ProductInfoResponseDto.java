package com.jeontongju.wishcart.dto.response;

import io.github.bitbox.bitbox.dto.ProductWishInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoResponseDto {

  private String productId;
  private String productName;
  private Long productPrice;
  private String productThumbnailImageUrl;
  private Boolean isSoldOut;
  private Boolean isActivate;

  public static ProductInfoResponseDto to(ProductWishInfoDto productWishInfoDto) {
    return ProductInfoResponseDto.builder()
        .productId(productWishInfoDto.getProductId())
        .productName(productWishInfoDto.getProductName())
        .productPrice(productWishInfoDto.getProductPrice())
        .productThumbnailImageUrl(productWishInfoDto.getProductThumbnailImage())
        .isSoldOut(productWishInfoDto.getStockQuantity() <= 0)
        .isActivate(productWishInfoDto.getIsActivate())
        .build();
  }
}
