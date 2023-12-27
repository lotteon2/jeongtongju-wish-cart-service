package com.jeontongju.wishcart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.bitbox.bitbox.dto.ProductWishInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoResponseDto {

  private String productId;
  private String productName;
  private Long productPrice;
  private String productThumbnailImageUrl;
  private Boolean isSoldOut;
  private Boolean isActivate;
  @JsonInclude(Include.NON_NULL)
  private Boolean isLikes;

  public static ProductInfoResponseDto to(ProductWishInfoDto productWishInfoDto) {
    return ProductInfoResponseDto.builder()
        .productId(productWishInfoDto.getProductId())
        .productName(productWishInfoDto.getProductName())
        .productPrice(productWishInfoDto.getProductPrice())
        .productThumbnailImageUrl(productWishInfoDto.getProductThumbnailImage())
        .isSoldOut(productWishInfoDto.getStockQuantity() <= 0)
        .isActivate(productWishInfoDto.getIsActivate())
        .isLikes(true)
        .build();
  }
}
