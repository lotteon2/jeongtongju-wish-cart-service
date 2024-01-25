package com.jeontongju.wishcart.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProcessor<T> {
  private final KafkaTemplate<String, T> kafkaTemplate;
  private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  public void send(String topic, T data) {
    kafkaTemplate.send(topic, data);
  }

  public void startListener(String id) {
    kafkaListenerEndpointRegistry.getListenerContainer(id).start();
  }

  public void stopListener(String id) {
    kafkaListenerEndpointRegistry.getListenerContainer(id).stop();
  }
}
