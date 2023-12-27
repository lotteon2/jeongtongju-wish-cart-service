package com.jeontongju.wishcart.client;

import io.github.bitbox.bitbox.dto.FeignFormat;
import io.github.bitbox.bitbox.dto.ProductIdListDto;
import io.github.bitbox.bitbox.dto.ProductWishInfoDto;
import java.util.HashMap;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductServiceFeignClient {
  @PostMapping("/wish-cart")
  FeignFormat<List<ProductWishInfoDto>> getProductInfo(@RequestBody ProductIdListDto productIdList);
  @PostMapping("/wish-cart/stock")
  FeignFormat<HashMap<String, Long>> getProductStock(@RequestBody ProductIdListDto productIdList);
}
