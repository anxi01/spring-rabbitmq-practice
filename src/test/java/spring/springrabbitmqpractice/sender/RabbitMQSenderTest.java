package spring.springrabbitmqpractice.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.springrabbitmqpractice.config.RabbitQueue;
import spring.springrabbitmqpractice.model.MyTask;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RabbitMQSenderTest {

  @Autowired
  private RabbitMessagePublisher publisher;

  @Test
  public void sendMsg() {
    String msg = "Hello World";
    publisher.publish(RabbitQueue.SAMPLE_TASK.getQueueName(), new MyTask(msg));
  }
}
