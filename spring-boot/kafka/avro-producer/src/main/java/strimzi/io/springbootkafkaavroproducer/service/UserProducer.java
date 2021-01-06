package strimzi.io.springbootkafkaavroproducer.service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import strimzi.io.avro.User;

@Service
public class UserProducer {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(UserProducer.class);

	  @Value("${topic.name}") 
	  private String TOPIC;

	  private final KafkaTemplate<String, User> kafkaTemplate;

	  @Autowired
	  public UserProducer(KafkaTemplate<String, User> kafkaTemplate) {   
	    this.kafkaTemplate = kafkaTemplate;
	  }

	  void sendMessage(User user) {
	    this.kafkaTemplate.send(this.TOPIC, (String) user.getName(), user);  
	    log.info(String.format("Produced user -> %s", user));
	  }
	  
	  @Scheduled(fixedRate = 5000)
	  public void produce() {
		  
		  List<String> givenList = Arrays.asList("hans", "peter", "paul","mary","joe");
		  Random rand = new Random();
		  String randomElement = givenList.get(rand.nextInt(givenList.size()));
		  User user = User.newBuilder()
				  .setName(randomElement)
				  .setAge(rand.ints(10, 100).findFirst().getAsInt())
				  .build();
		  /*
		  User user = new User();
		  user.setName(randomElement);
		  user.setAge(rand.ints(10, 100).findFirst().getAsInt());
		  */
		  this.sendMessage(user);
		
	  }

}
