package com.jeontongju.wishcart.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
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
@DynamoDBTable(tableName = "cart")
public class Cart {

  @Id
  private ConsumerCompositeKey cartId;

  @DynamoDBHashKey(attributeName = "consumer_id")
  public Long getConsumerId() {
    return cartId != null ? cartId.getConsumerId() : null;
  }

  public void setConsumerId(Long consumerId) {
    if (cartId == null) {
      cartId = new ConsumerCompositeKey();
    }
    cartId.setConsumerId(consumerId);
  }

  @DynamoDBRangeKey(attributeName = "created_at")
  public String getCreatedAt() {
    return cartId != null ? cartId.getCreatedAt() : null;
  }

  public void setCreatedAt(String createdAt) {
    if (cartId == null) {
      cartId = new ConsumerCompositeKey();
    }
    cartId.setCreatedAt(createdAt);
  }

  @DynamoDBAttribute(attributeName = "product_id")
  private String productId;

  @DynamoDBAttribute(attributeName = "amount")
  private Long amount;

}
