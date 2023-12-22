package com.jeontongju.wishcart.repository;

import com.jeontongju.wishcart.domain.Cart;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CartRepository extends CrudRepository<ConsumerCompositeKey, Cart> {

}
