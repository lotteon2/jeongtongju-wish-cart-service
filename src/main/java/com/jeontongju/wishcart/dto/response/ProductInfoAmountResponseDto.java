package com.jeontongju.wishcart.dto.response;

import io.github.bitbox.bitbox.dto.ProductWishInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoAmountResponseDto extends ProductInfoResponseDto {
  private Long amount;

  public static ProductInfoAmountResponseDto to(ProductWishInfoDto productWishInfoDto) {
    return ProductInfoAmountResponseDto.builder()
        .productId(productWishInfoDto.getProductId())
        .productName(productWishInfoDto.getProductName())
        .productPrice(productWishInfoDto.getProductPrice())
        .productThumbnailImageUrl(productWishInfoDto.getProductThumbnailImage())
        .isSoldOut(productWishInfoDto.getStockQuantity() <= 0)
        .isActivate(productWishInfoDto.getIsActivate())
        .amount(productWishInfoDto.getStockQuantity())
        .build();
  }
}
