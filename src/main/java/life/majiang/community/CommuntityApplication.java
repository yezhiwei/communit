package life.majiang.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("life.majiang.community.mapper")
public class CommuntityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommuntityApplication.class, args);
	}

}
