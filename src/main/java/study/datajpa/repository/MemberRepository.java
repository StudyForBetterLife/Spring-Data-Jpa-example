package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * JpaRepository 인터페이스를 상속한다.
 * 그럼 인터페이스에 대한 구현체는 어디에?
 * -> 스프링이 인터페이스에 대한 프록시 클래스를 만들고 (memberRepository.getClass() class com.sun.proxy.$ProxyXXX,
 * Spring Data JPA가 구현체를 알아서 만든 뒤 inject 한다
 * <p>
 * <p>
 * [제네릭 타입]
 * T : 엔티티
 * ID : 엔티티의 식별자 타입
 * S : 엔티티와 그 자식 타입
 * [주요 메서드]
 * save(S) : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다.
 * delete(T) : 엔티티 하나를 삭제한다. 내부에서 EntityManager.remove() 호출
 * findById(ID) : 엔티티 하나를 조회한다. 내부에서 EntityManager.find() 호출
 * getOne(ID) : 엔티티를 프록시로 조회한다. 내부에서 EntityManager.getReference() 호출
 * findAll(…) : 모든 엔티티를 조회한다. 정렬( Sort )이나 페이징( Pageable ) 조건을 파라미터로 제공할 수
 * 있다
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * List<Member> findByUsername(String username);
     * 를 구현하지 않아도 동작한다.
     * HOW? -> 쿼리 메소드 기능을 통해 동작한다.
     * <p>
     * [쿼리 메소드 기능]
     * 메소드 이름으로 쿼리 생성
     * NamedQuery
     * "@Query" : 리파지토리 메소드에 쿼리 정의
     * 파라미터 바인딩
     * 반환 타입
     * 페이징과 정렬
     * 벌크성 수정 쿼리
     * "@EntityGraph"
     * <p>
     * [쿼리 메소드 기능 3가지]
     * 1.메소드 이름으로 쿼리 생성
     * 2.메소드 이름으로 JPA NamedQuery 호출
     * 3.@Query 어노테이션을 사용해서 리파지토리 인터페이스에 쿼리 직접 정의
     */
    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
