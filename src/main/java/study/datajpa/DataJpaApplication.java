package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

/**
 * @EnableJpaRepositories(basePackages = "study.datajpa.repository")
 * Spring Data JPA를 사용하면 해당 어노테이션을 작성해야한다
 * -> 스프링 부트를 사용하면 생략 가능
 * (만약 repository 의 위치가 변경되면 어노테이션으로 명시해줘야 한다.)
 *
 * @EnableJpaAuditing
 * Auditing 기능 추가 (BaseEntity)
 */
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	/**
	 * BaseEntity에서
	 * createdBy, lastModifiedBy 필드에 들어갈 정보를 세팅한다.
	 *
	 * 실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID를 받는다.
	 * @return
	 */
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
