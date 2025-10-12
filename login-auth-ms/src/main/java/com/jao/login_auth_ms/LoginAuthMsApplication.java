package com.jao.login_auth_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.jao.login_auth_ms")
@EnableJpaRepositories(basePackages = "com.jao.login_auth_ms.infrastructure.adapter.out.persistence.repository") 
@EntityScan(basePackages = "com.jao.login_auth_ms.infrastructure.adapter.out.persistence.entity") 
@ComponentScan(basePackages = {
	    "com.jao.login_auth_ms" // Escanea todo bajo la raíz del proyecto
	})
public class LoginAuthMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginAuthMsApplication.class, args);
	}

}
