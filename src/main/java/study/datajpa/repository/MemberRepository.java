package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
     * <p>
     * [쿼리 메소드 작성 방법](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)
     * 조회: find…By ,read…By ,query…By get…By,
     * COUNT: count…By 반환타입 long
     * EXISTS: exists…By 반환타입 boolean
     * 삭제: delete…By, remove…By 반환타입 long
     * DISTINCT: findDistinct, findMemberDistinctBy
     * LIMIT: findFirst3, findFirst, findTop, findTop3
     * <p>
     * 단, 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다
     */
//    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy(); // find...By : ...에 아무거나 넣어도 된다.

    /**
     * Member 엔티티에서
     *
     * @NamedQuery( name = "Member.findByUsername",
     * query = "select m from Member m where m.username = :username"
     * )
     * 에 대한 메소드이다.
     * -> 실행되면 Member의 @NamedQuery중 "Member.findByUsername"를 찾아간다.
     * <p>
     * "@Query(name = "Member.findByUsername")"
     * -> 해당 어노테이션이 없어도 Spring Data JPA가 알아서 찾는다.
     * [찾는 과정]
     * 1. Member 엔티티속 선언한 "도메인 클래스 + .(점) + 메서드 이름"으로 Named 쿼리를 찾아서 실행
     * 2. 약 실행할 Named 쿼리가 없으면 메서드 이름으로 쿼리 생성 전략을 사용한다.
     * <p>
     * 하지만, 실무에서 NamedQuery를 잘 사용하지 않는다.
     * -> Repository 메소드에 쿼리를 바로 작성할 수 있다.
     * (@Query, 리포지토리 메소드에 쿼리 정의하기)
     */
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    /**
     * @Query, 리포지토리 메소드에 쿼리 정의하기 = 이름이 없는 NamedQuery
     * -> [쿼리 메소드 기능]을 사용하면 메소드 이름이 너무 길어진다.
     * -> 이름을 간결하게 정의할 수 있다.
     * <p>
     * 만약, @Query 에 작성한 쿼리 문법이 틀리면 어플리케이션 로딩 시점에 오류를 발생시킨다.
     * <p>
     * 따라서,
     * [쿼리 메소드 기능] : 간단하게 사용할 때,
     * [@Query, 리포지토리 메소드에 쿼리 정의] : 복잡한 쿼리를 사용할 때
     * <p>
     * 동적 쿼리는? -> QueryDSL 사용해라
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 단순히 값 하나만 조ㅚ
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * @Query, 값, DTO 조회하기
     */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * 파라미터 바인딩 (위치 기반은 사용하지 말고, 가급적 이름 기반으로 사용해라)
     * <p>
     * [컬렉션 파라미터 바인딩]
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //컬렉션 (findListByUsername의 결과가 없으면 빈 컬랙션을 반환한다.)
    List<Member> findListByUsername(String name);

    //단건 (findMemberByUsername의 결과가 없으면 null을 반환한다.)
    Member findMemberByUsername(String name);

    //단건 Optional (결과 데이터의 유무 판단이 확실하지 않다면 Optinal을 사용해라)
    Optional<Member> findOptionalByUsername(String name);

    /**
     * 스프링 데이터 JPA 페이징과 정렬
     *
     * <p>
     * [페이징과 정렬 파라미터]
     * org.springframework.data.domain.Sort : 정렬 기능
     * org.springframework.data.domain.Pageable : 페이징 기능 (내부에 Sort 포함)
     *
     * <p>
     * [특별한 반환 타입]
     * org.springframework.data.domain.Page : 추가 count 쿼리 결과를 포함하는 페이징
     * org.springframework.data.domain.Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능
     * (내부적으로 limit + 1조회)
     * List (자바 컬렉션): 추가 count 쿼리 없이 결과만 반환
     *
     * <p>
     * 자동으로 total count를 구하는 쿼리도 날린다.
     * -> Pagable은 org.springframework.data.domain.PageRequest 객체를 사용한다.
     *
     * <p>
     * [Page 인터페이스]
     * int getTotalPages(); //전체 페이지 수
     * long getTotalElements(); //전체 데이터 수
     * <U> Page<U> map(Function<? super T, ? extends U> converter); //변환기
     */
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * [Slice 인터페이스]
     * int getNumber(); //현재 페이지
     * int getSize(); //페이지 크기
     * int getNumberOfElements(); //현재 페이지에 나올 데이터 수
     * List<T> getContent(); //조회된 데이터
     * boolean hasContent(); //조회된 데이터 존재 여부
     * Sort getSort(); //정렬 정보
     * boolean isFirst(); //현재 페이지가 첫 페이지 인지 여부
     * boolean isLast(); //현재 페이지가 마지막 페이지 인지 여부
     * boolean hasNext(); //다음 페이지 여부
     * boolean hasPrevious(); //이전 페이지 여부
     * Pageable getPageable(); //페이지 요청 정보
     * Pageable nextPageable(); //다음 페이지 객체
     * Pageable previousPageable();//이전 페이지 객체
     * <U> Slice<U> map(Function<? super T, ? extends U> converter); //변환기
     */
    @Query(
            value = "select m from Member m left join m.team t", // content를 가져오는 쿼리
            countQuery = "select count(m.username) from Member m" // 카운트 쿼리
    )
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    List<Member> findTop3By();

    /**
     * 스프링 데이터 JPA를 사용한 벌크성 수정 쿼리
     *
     * @Modifying -> 업데이트 쿼리에서 @Modifying 어노테이션을 달아야 한다.
     * @Modifying(clearAutomatically = true) -> em.flush, em.clear 를 자동으로 해준다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * fetch join을 하면
     * -> Member에서 LAZY 설정된 참조 값에 대한 데이터를 한번에 가져온다. (지연로딩 해제됨)
     *
     * @return
     */
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    /**
     * [공통 메서드 오버라이드]
     *
     * @EntityGraph(attributePaths = {"team"})
     * <p>
     * List<Member> findAll();
     * -> JpaRepository의 기본 인터페이스 메소드이다 (그래서 Override)
     * -> 기본적인 findAll을 사용하고 싶은데, Team 객체에 대해서 LAZY 로딩 설정을 해제하고 싶은 경우
     * @EntityGraph(attributePaths = {"team"}) 어노테이션을 달아준다.
     * (내부적으로 fetch join을 사용하는 것)
     */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    /**
     * [JPQL + 엔티티 그래프]
     */
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    /**
     * [메서드 이름으로 쿼리에서 특히 편리하다.]
     */
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    /**
     * [JPA Hint & Lock]
     * <p>
     * JPA Hint : JPA 쿼리 힌트(SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트)
     *
     * @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
     * -> JPA에게 findReadOnlyByUsername메소드는 readOnly이므로
     * dirty checking을 위한 스냅샷(원본 데이터)를 만들지 않는다
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    /**
     * [Lock] : JPA가 제공하는 Lock 을 활용할 수 있다.
     *
     *     select
     *         member0_.member_id as member_i1_0_,
     *         member0_.age as age2_0_,
     *         member0_.team_id as team_id4_0_,
     *         member0_.username as username3_0_
     *     from
     *         member member0_
     *     where
     *         member0_.username=? for update
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);
}
