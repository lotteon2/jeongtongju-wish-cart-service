package com.jeontongju.wishcart.repository;

import com.jeontongju.wishcart.domain.Wish;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface WishRepository extends CrudRepository<Wish, Long> {

}
