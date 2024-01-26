package com.jeontongju.wishcart.execption.advice;

import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.execption.DynamoDBThrottlingException;
import com.jeontongju.wishcart.execption.InvalidAmountException;
import com.jeontongju.wishcart.execption.PageExceededException;
import com.jeontongju.wishcart.execption.RetrySchedulingException;
import com.jeontongju.wishcart.execption.StockOverException;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import io.github.bitbox.bitbox.enums.FailureTypeEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ControllerAdvice extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return ResponseEntity
        .status(status.value())
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(
                    e.getBindingResult().getFieldError() == null
                        ? e.getMessage()
                        : e.getBindingResult().getFieldError().getDefaultMessage())
                .build()
        );
  }

  @ExceptionHandler(CartNotFoundException.class)
  public ResponseEntity<ResponseFormat<Void>> handleCartNotFoundException(
      CartNotFoundException e
  ) {
    HttpStatus status = HttpStatus.OK;

    return ResponseEntity
        .ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .build()
        );
  }

  @ExceptionHandler(WishNotFoundException.class)
  public ResponseEntity<ResponseFormat<Void>> handleWishNotFoundException(
      WishNotFoundException e
  ) {
    HttpStatus status = HttpStatus.OK;

    return ResponseEntity
        .ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .build()
        );
  }

  @ExceptionHandler(PageExceededException.class)
  public ResponseEntity<ResponseFormat<Void>> handlePageExceededException(
      PageExceededException e
  ) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    return ResponseEntity
        .status(status.value())
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .build()
        );
  }

  @ExceptionHandler(StockOverException.class)
  public ResponseEntity<ResponseFormat<Long>> handleStockOverException(
      StockOverException e
  ) {
    HttpStatus status = HttpStatus.OK;

    return ResponseEntity
        .ok()
        .body(
            ResponseFormat.<Long>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .failure(FailureTypeEnum.STOCK_OVER)
                .data(e.getStock())
                .build()
        );
  }

  @ExceptionHandler(InvalidAmountException.class)
  public ResponseEntity<ResponseFormat<Void>> handleInvalidAmountException(
      InvalidAmountException e
  ) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    return ResponseEntity
        .status(status.value())
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .build()
        );
  }

  @ExceptionHandler(ProvisionedThroughputExceededException.class)
  public ResponseEntity<ResponseFormat<Void>> handleProvisionedThroughputExceededException(
      ProvisionedThroughputExceededException e
  ) {
    HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;

    return ResponseEntity
        .status(status.value())
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail("요청이 너무 많습니다. 잠시 후 다시 시도해주세요.")
                .build()
        );
  }

  @ExceptionHandler(DynamoDBThrottlingException.class)
  public ResponseEntity<ResponseFormat<Void>> handleDynamoDBThrottlingException(
      DynamoDBThrottlingException e
  ) {
    HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;

    return ResponseEntity
        .status(status.value())
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .build()
        );
  }

  @ExceptionHandler(RetrySchedulingException.class)
  public ResponseEntity<ResponseFormat<Void>> handleSchedulerException(
      RetrySchedulingException e
  ) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    return ResponseEntity
        .status(status.value())
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(e.getMessage())
                .build()
        );
  }
}
