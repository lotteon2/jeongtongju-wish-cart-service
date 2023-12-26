package com.jeontongju.wishcart.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Set;
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
  @DynamoDBHashKey(attributeName = "consumer_id")
  private Long consumerId;

  @DynamoDBAttribute(attributeName = "products")
  private Set<String> products;

}
