package spring.springrabbitmqpractice.config;

public enum RabbitQueue {

  SAMPLE_TASK("photo.sample"),
  READ("photo.read"),
  WRITE("photo.write"),
  EMPTY("photo.empty");

  private final String queueName;

  RabbitQueue(String queueName) {
    this.queueName = queueName;
  }

  public String getQueueName() {
    return queueName;
  }

  public static RabbitQueue find(String name) {
    for (RabbitQueue queue : RabbitQueue.values()) {
      if (queue.getQueueName().equalsIgnoreCase(name)) {
        return queue;
      }
    }
    return RabbitQueue.EMPTY;
  }
}
