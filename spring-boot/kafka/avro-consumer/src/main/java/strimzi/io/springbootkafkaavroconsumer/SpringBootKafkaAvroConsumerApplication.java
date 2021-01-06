package strimzi.io.springbootkafkaavroconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SpringBootKafkaAvroConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaAvroConsumerApplication.class, args);
	}

}
