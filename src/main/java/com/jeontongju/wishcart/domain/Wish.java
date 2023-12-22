package com.jeontongju.wishcart.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.jeontongju.wishcart.vo.ConsumerCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "wish")
public class Wish {

  @Id
  private ConsumerCompositeKey wishId;

  @DynamoDBHashKey(attributeName = "consumer_id")
  public Long getConsumerId() {
    return wishId != null ? wishId.getConsumerId() : null;
  }

  public void setConsumerId(Long consumerId) {
    if (wishId == null) {
      wishId = new ConsumerCompositeKey();
    }
    wishId.setConsumerId(consumerId);
  }

  @DynamoDBHashKey(attributeName = "createdAt")
  public String getCreatedAt() {
    return wishId != null ? wishId.getCreatedAt() : null;
  }

  public void setCreatedAt(String createdAt) {
    if (wishId == null) {
      wishId = new ConsumerCompositeKey();
    }
    wishId.setCreatedAt(createdAt);
  }

  @DynamoDBAttribute(attributeName = "product_id")
  private String productId;

}
