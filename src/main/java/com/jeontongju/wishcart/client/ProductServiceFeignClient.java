package com.jeontongju.wishcart.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductServiceFeignClient {

}
