package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @EnableJpaRepositories(basePackages = "study.datajpa.repository")
 * Spring Data JPA를 사용하면 해당 어노테이션을 작성해야한다
 * -> 스프링 부트를 사용하면 생략 가능
 * (만약 repository 의 위치가 변경되면 어노테이션으로 명시해줘야 한다.)
 */
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

}
