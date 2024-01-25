package com.jeontongju.wishcart.execption;

import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;

public class DynamoDBThrottlingException extends ProvisionedThroughputExceededException {
  public static final String message = "요청이 너무 많아 잠시 후 처리될 예정입니다.";

  public DynamoDBThrottlingException() {
    super(message);
  }
}
