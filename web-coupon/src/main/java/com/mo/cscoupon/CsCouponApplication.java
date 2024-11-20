package com.mo.cscoupon;

import com.mo.cscore.CoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CoreConfig.class)
@SpringBootApplication
public class CsCouponApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-coupon, application-core");
		SpringApplication.run(CsCouponApplication.class, args);
	}

}
