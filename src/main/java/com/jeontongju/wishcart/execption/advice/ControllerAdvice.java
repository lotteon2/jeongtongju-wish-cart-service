package com.jeontongju.wishcart.execption.advice;

import com.jeontongju.wishcart.execption.CartNotFoundException;
import com.jeontongju.wishcart.execption.PageExceededException;
import com.jeontongju.wishcart.execption.WishNotFoundException;
import io.github.bitbox.bitbox.dto.ResponseFormat;
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
}
