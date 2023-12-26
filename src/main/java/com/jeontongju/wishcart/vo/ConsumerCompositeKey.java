package com.jeontongju.wishcart.vo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTyped(DynamoDBAttributeType.M)
public class ConsumerCompositeKey implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long consumerId;
  private String productId;

  @DynamoDBHashKey(attributeName = "consumer_id")
  public Long getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(Long consumerId) {
    this.consumerId = consumerId;
  }

  @DynamoDBRangeKey(attributeName = "product_id")
  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public static ConsumerCompositeKey of(Long consumerId, String productId) {
    return ConsumerCompositeKey.builder()
        .consumerId(consumerId)
        .productId(productId)
        .build();
  }
}
