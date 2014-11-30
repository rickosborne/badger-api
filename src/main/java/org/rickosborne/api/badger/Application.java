package org.rickosborne.api.badger;

import org.rickosborne.api.badger.data.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
//@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableWebMvc
@Configuration
@ComponentScan
public class Application extends RepositoryRestMvcConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
