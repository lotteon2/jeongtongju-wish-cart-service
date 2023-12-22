package com.jeontongju.wishcart.repository;

import com.jeontongju.wishcart.domain.Wish;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface WishRepository extends CrudRepository<ConsumerCompositeKey, Wish> {

}
