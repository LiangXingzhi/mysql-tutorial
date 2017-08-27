package lxz.tutorial.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MysqlApplication {

  public static void main(String[] args) {
    SpringApplication.run(MysqlApplication.class, args);
  }
}
