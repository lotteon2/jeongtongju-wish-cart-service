package com.jeontongju.wishcart.vo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import java.io.Serializable;
import java.time.LocalDate;
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
  private String createdAt;

  @DynamoDBHashKey(attributeName = "consumer_id")
  public Long getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(Long consumerId) {
    this.consumerId = consumerId;
  }

  @DynamoDBHashKey(attributeName = "created_at")
  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public static ConsumerCompositeKey of(Long consumerId) {
    return ConsumerCompositeKey.builder()
        .consumerId(consumerId)
        .createdAt(LocalDate.now().toString())
        .build();
  }
}
