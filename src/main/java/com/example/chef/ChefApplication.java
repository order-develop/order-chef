package com.example.chef;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@MapperScan("com.example.chef.dao.mapper")
//@EnableAutoConfiguration
@EnableSwagger2
public class ChefApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChefApplication.class, args);
    }

}

