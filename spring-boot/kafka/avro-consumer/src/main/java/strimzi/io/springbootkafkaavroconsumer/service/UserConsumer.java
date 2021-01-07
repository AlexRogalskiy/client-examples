package strimzi.io.springbootkafkaavroconsumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import strimzi.io.avro.User;

@Service
public class UserConsumer {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(UserConsumer.class);

	  @Value("${topic.name}") 
	  private String TOPIC;
	  
	  @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")   
	  public void consume(ConsumerRecord<String, User> record) {
		  User user = record.value();
		  log.info(String.format("Consumed message -> %s", user.toString()));
	  }

}
