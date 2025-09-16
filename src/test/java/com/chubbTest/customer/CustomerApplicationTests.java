package com.chubbTest.customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.chubbTest.customer.infrastructure.config.spring.CustomerApplication;

@SpringBootTest(classes = CustomerApplication.class)
@ActiveProfiles("test")
class CustomerApplicationTests {

	@Test
	void contextLoads() {
	}

}
