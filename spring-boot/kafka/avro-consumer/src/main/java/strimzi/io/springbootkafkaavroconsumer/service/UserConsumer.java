package strimzi.io.springbootkafkaavroconsumer.service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import strimzi.io.avro.User;

@Service
public class UserConsumer {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(UserConsumer.class);

	  @Value("${topic.name}") 
	  private String TOPIC;
	  
	  @KafkaListener(topics = "avro-sample", groupId = "avro-sample")   
	  public void consume(ConsumerRecord<String, User> record) {
	    log.info(String.format("Consumed message -> %s", record.value()));
	  }

}
