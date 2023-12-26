package com.jeontongju.wishcart.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
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
@Builder(toBuilder = true)
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

  @DynamoDBRangeKey(attributeName = "product_id")
  public String getProductId() {
    return cartId != null ? cartId.getProductId() : null;
  }

  public void setProductId(String productId) {
    if (cartId == null) {
      cartId = new ConsumerCompositeKey();
    }
    cartId.setProductId(productId);
  }

  @DynamoDBAttribute(attributeName = "amount")
  private Long amount;

  @DynamoDBAttribute(attributeName = "created_at")
  private String created_at;

}
