package spring.springrabbitmqpractice.config;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.springrabbitmqpractice.model.MyTask;
import spring.springrabbitmqpractice.utils.JacksonConverter;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

  /**
   * RabbitProperties : 아이디나 비밀번호를 넣을 수 있는 값
   */
  @Resource
  private RabbitProperties rabbitProperties;

  public static final String RABBIT_EXCHANGE_NAME = "hololo"; // 큐들을 매핑할 키값
  private static final Integer CONSUMER_COUNT = 5;

  /**
   * ClassMapper : 객체를 주고 받는데 객체로 사용할 값을 DefaultClassMapper 를 통해 저장할 수 있다.
   * MyTask 클래스를 RabbitMQ를 통해서 객체를 주고 받을 것을 명시
   */
  @Bean
  public DefaultClassMapper defaultClassMapper() {
    DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
    Map<String, Class<?>> idClassMapping = new HashMap<>();
    idClassMapping.put("mytask", MyTask.class);
    defaultClassMapper.setIdClassMapping(idClassMapping);
    return defaultClassMapper;
  }

  /**
   * 객체를 전송할 때 바이트 배열로 직렬화하여 메시지 큐로 보내고, 수신할 때는 다시 객체로 역직렬화하기 때문에
   * Jackson2JsonMessageConverter 를 이용해 직렬화 및 역직렬화를 한다.
   */
  @Bean
  public MessageConverter rabbitMessageConverter() {
    Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter(JacksonConverter.getInstance());
    jsonConverter.setClassMapper(defaultClassMapper());
    return jsonConverter;
  }

  /**
   * 실제로 연동하는 정보
   * 커넥션 객체를 만든 다음에 여기서 유저 정보, 패스워드를 설정한다.
   */
  @Bean
  public ConnectionFactory rabbitConnectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setUsername(rabbitProperties.getUsername());
    connectionFactory.setPassword(rabbitProperties.getPassword());
    connectionFactory.setCacheMode(CacheMode.CHANNEL);
    return connectionFactory;
  }

  /**
   * Admin 설정 관련 부분
   * RabbitMQ에 admin 권한이 있는 계정으로 접속한 후에
   * exchange 와 queue 를 등록하고 매핑한다.
   * @param rabbitConnectionFactory
   * @return
   */
  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory rabbitConnectionFactory) {
    final RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);

    // exchange 등록
    rabbitExchange(rabbitAdmin);

    // queue 자동 등록
    for (RabbitQueue rabbitQueue : RabbitQueue.values()) {
      rabbitAdmin.declareQueue(new Queue(rabbitQueue.getQueueName(), true)); // 큐 등록
      rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(rabbitQueue.getQueueName(), true)) // exchange 에 바인딩
          .to(rabbitExchange(rabbitAdmin)).with(rabbitQueue.getQueueName()));
    }

    rabbitAdmin.afterPropertiesSet();
    return rabbitAdmin;
  }

  /**
   * TopicExchange 는 메시지를 라우팅할 때 특정 주제를 기반으로 메시지를 전달하는 exchange
   * TopicExchange 는 라우팅 키 패턴을 이용해 메시지를 다양한 큐에 동적으로 전달할 수 있다.
   * @param rabbitAdmin
   * @return
   */
  @Bean
  TopicExchange rabbitExchange(RabbitAdmin rabbitAdmin) {
    TopicExchange topicExchange = new TopicExchange(RABBIT_EXCHANGE_NAME);
    topicExchange.setAdminsThatShouldDeclare(rabbitAdmin);
    return topicExchange;
  }
}
