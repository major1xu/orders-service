package com.workshop.ordersservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Stream;

@RepositoryRestResource
interface OrderRepository extends PagingAndSortingRepository<OrderItem, Long> {

    @RestResource(path = "by-name")
    Collection<OrderItem> findByCustomerName(@Param("customerName") String customerName);
}

@RefreshScope
@RestController
class MessageRestController {

    @Value("${greeting}")
    private String greeting;

    @RequestMapping("/greeting")
    String getGreeting() {
        System.out.println("greeting endpoint called");
        return this.greeting;
    }

	@Value("${message}")
	private String message;
	
	//@Value("${spring.cloud.config.server.git.uri}")
    //private String giturl;

	@RequestMapping("/message")
	String getMessage() {
		System.out.println("Message endpoint called");
		return this.message //+ this.giturl
				;
	}
	
}

@SpringBootApplication
@EnableAutoConfiguration
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(OrderRepository orderRepository) {
        return args -> {
            Stream.of("47774", "77333", "99333", "27772")
                    .forEach(orderNumber -> orderRepository.save(new OrderItem(orderNumber, "Customer1")));
            orderRepository.findAll().forEach(System.out::println);
        };
    }

    @Configuration
    public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/**");
        }
    }
}
