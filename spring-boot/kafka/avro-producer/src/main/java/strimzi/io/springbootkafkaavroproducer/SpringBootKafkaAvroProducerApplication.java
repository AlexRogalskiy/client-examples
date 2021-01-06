package strimzi.io.springbootkafkaavroproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootKafkaAvroProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaAvroProducerApplication.class, args);
	}

}
