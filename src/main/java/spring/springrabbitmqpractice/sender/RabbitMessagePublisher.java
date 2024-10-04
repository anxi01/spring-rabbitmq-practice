package spring.springrabbitmqpractice.sender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import spring.springrabbitmqpractice.model.MyTask;

@Component
public class RabbitMessagePublisher {

  private static final Logger log = LogManager.getLogger(RabbitMessagePublisher.class);
  private final RabbitTemplate rabbitTemplate;

  public RabbitMessagePublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * RabbitTemplate : Sender
   * rabbitTemplate.convertAndSend() 를 통해 전송한다.
   * routingKey 는 통상 queueName 과 맞춰 쓴다.
   * @param routingKey
   * @param myTask
   */
  public void publish(String routingKey, MyTask myTask) {
    try {
      rabbitTemplate.convertAndSend("hololo", routingKey, myTask);
    } catch (Exception e) {
      log.error("error", e);
    }
  }
}
