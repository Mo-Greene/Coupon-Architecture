package com.mo.csbatch;

import com.mo.cscore.CoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CoreConfig.class)
@SpringBootApplication
public class CsBatchApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-batch, application-core");
		SpringApplication.run(CsBatchApplication.class, args);
	}

}
