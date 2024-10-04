package spring.springrabbitmqpractice.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import spring.springrabbitmqpractice.model.MyTask;

@Component
public class RabbitMQReceiver {

  private static final Logger log = LogManager.getLogger(RabbitMQReceiver.class);

  /**
   * Receiver 를 만드는 가장 쉬운 방법 : annotation 사용
   * @param myTask
   */
  @RabbitListener(id = "photo.sample", queues = "photo.sample")
  public void handle(MyTask myTask) {
    log.info("mydata handle :: {}", myTask.toString());
  }
}
