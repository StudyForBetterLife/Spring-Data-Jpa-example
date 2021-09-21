package study.datajpa.repository;

/**
 * Projections
 * <p>
 * 엔티티 대신에 DTO를 편리하게 조회할 때 사용
 * 전체 엔티티가 아니라 만약 회원 이름만 딱 조회하고 싶으면?
 * 프로젝션을 사용하면 된다.
 * <p>
 * 프로젝션 대상이 root 엔티티면 유용하다.
 * 프로젝션 대상이 root 엔티티를 넘어가면 JPQL SELECT 최적화가 안된다!
 * 실무의 복잡한 쿼리를 해결하기에는 한계가 있다.
 * 실무에서는 단순할 때만 사용하고, 조금만 복잡해지면 QueryDSL을 사용하자
 */
public interface UsernameOnly {
    String getUsername(); // 메소드의 구현체는 Spring Data JPA가 작성해준다.
}
