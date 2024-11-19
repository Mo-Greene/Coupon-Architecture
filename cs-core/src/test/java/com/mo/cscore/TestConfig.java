package com.mo.cscore;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.config.name=application-test"})
@SpringBootTest(classes = CoreConfig.class)
public class TestConfig {
}
