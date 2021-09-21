package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() throws Exception {
        // given
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        // when
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCRUD() throws Exception {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // dirty checking 으로 인해 데이터 변경을 인지하여
        // -> update 쿼리를 보내 데이터베이스의 값을 변경된다.
        findMember1.setUsername("member!@#!@#");

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void testfindByUsernameAndAgeGreaterThan() throws Exception {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        // when

        List<Member> all = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        // then
        assertThat(all.get(0).getUsername()).isEqualTo("AAA");
        assertThat(all.get(0).getAge()).isEqualTo(20);
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void findHelloByTest() throws Exception {
        // given
        List<Member> helloBy = memberRepository.findTop3HelloBy(); // limit 3;
        // when

        // then

    }

    @Test
    public void testNamedQuery() throws Exception {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        // when
        List<Member> result = memberRepository.findByUsername("AAA");
        // then
        Member member = result.get(0);
        assertThat(member).isEqualTo(m1);

    }

    @Test
    public void testQuery() throws Exception {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        // when
        List<Member> result = memberRepository.findUser("AAA", 10);
        // then
        assertThat(result.get(0)).isEqualTo(m1);

    }

    @Test
    public void findUsernameList() throws Exception {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // then
        for (String s : memberRepository.findUsernameList()) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void findMemberDto() throws Exception {
        // given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        // then
        for (MemberDto dto : memberRepository.findMemberDto()) {
            System.out.println("dto = " + dto);
        }

    }

    @Test
    public void findByNames() throws Exception {
        // given

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // then
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member. = " + member);
        }


    }

    @Test
    public void returnType() throws Exception {
        // given

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // then
        List<Member> aaa = memberRepository.findListByUsername("AAA");
        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");

        System.out.println("aaa = " + aaa);
        System.out.println("aaa1 = " + aaa1);
        System.out.println("aaa2 = " + aaa2);


    }

    @Test
    public void paging() throws Exception {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        /**
         * Page는 1부터 시작이 아니라 0부터 시작이다.
         * (public interface Page<T> extends Slice<T>)
         */
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); // totalCount와 같다
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?

        /**
         * Page의 유일한 단점??
         * -> totalCount를 하기위해서 필요없는 쿼리(ex) join 쿼리)까지 나갈 수 있다. (성능 이슈)
         * -> 카운트 쿼리 분리(이건 복잡한 sql에서 사용, 데이터는 left join, 카운트는 left join 안해도 됨 : 실무에서 매우 중요!!!)
         *     @Query(
         *             value = "select m from Member m left join m.team t", // content를 가져오는 쿼리
         *             countQuery = "select count(m.username) from Member m" // 카운트 쿼리
         *     )
         *
         * 그리고
         * Page로 API를 반환하면 안된다.
         * -> 엔티티를 외부에 노출하면 안된다 => DTO로 변환해라
         * ==> Page<MemberDto> toMap 를 반환해라!!
         */
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

    }

    @Test
    public void slicing() throws Exception {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));        // when

        // when
        /**
         * Slice 는 다음 데이터 1개를 더 가져온다.
         */
        Slice<Member> page2 = memberRepository.findSliceByAge(age, pageRequest);

        // then
        assertThat(page2.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page2.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page2.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        /**
         * [벌크연산에서 조심해야 할 점]
         * -> 데이터베이스에는 member5의 나이가 41이지만
         * -> 영속성 컨택스트에서 member5의 나이는 40이다. (아직 flush가 되지 않아서)
         *
         * -> em.flush()
         * em.clear()를 해줘야 한다.
         */
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5); // age = 40

        em.flush();
        em.clear();

        List<Member> resultFlush = memberRepository.findByUsername("member5");
        Member flushedMember = resultFlush.get(0);
        System.out.println("flushedMember = " + flushedMember); // age 41

        /**
         * em.flush();
         * em.clear();
         * -> flush 코드 없이 repository에서
         * @Modifying(clearAutomatically = true) 설정해주면 된다.
         */

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    /**
     * @EntityGraph : 연관된 엔티티들을 SQL 한번에 조회하는 방법
     * -> 사실상 페치 조인(FETCH JOIN)의 간편 버전이다.
     */
    @Test
    public void findMemberLazy() throws Exception {
        // given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        System.out.println("=== N + 1 문제 시작 ===");
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            // member.getTeam().getClass() = class study.datajpa.entity.Team$HibernateProxy$2gxC2Mbr
            // 디비에서 Member만 가져온 뒤, Team(LAZY 설정)에는 프록시 객체를 설정한다.
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            // Team 객체를 건들일 때, Team 데이터를 가져오는 쿼리가 나간다. (member team은 지연로딩 관계이다)
            // 만약, LAZY 설정된 참조 객체가 N개라면 N개에 대한 쿼리가 추가적으로 나간다. (team의 데이터를 조회할 때 마다 쿼리가
            //실행된다)
            // N + 1 문제 발생
            // -> fetch join으로 해결
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }

        System.out.println("=== fetch join 시작 ===");
        List<Member> memberFetchJoin = memberRepository.findMemberFetchJoin();
        for (Member memberFetch : memberFetchJoin) {
            System.out.println("memberFetch.getUsername() = " + memberFetch.getUsername());
            System.out.println("memberFetch.getTeam().getClass() = " + memberFetch.getTeam().getClass());
            System.out.println("memberFetch.getTeam().getName() = " + memberFetch.getTeam().getName());
        }
    }

    @Test
    public void findMemberFetchJoin() throws Exception {
        // given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        System.out.println("=== fetch join 시작 ===");
        List<Member> memberFetchJoin = memberRepository.findMemberFetchJoin();
        for (Member memberFetch : memberFetchJoin) {
            System.out.println("memberFetch.getUsername() = " + memberFetch.getUsername());
            // memberFetch.getTeam().getClass() = class study.datajpa.entity.Team
            // 프록시가 아닌 순수한 엔티티
            System.out.println("memberFetch.getTeam().getClass() = " + memberFetch.getTeam().getClass());
            System.out.println("memberFetch.getTeam().getName() = " + memberFetch.getTeam().getName());
        }
    }

    @Test
    public void findEntityGraphByUsername() throws Exception {
        // given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        System.out.println("=== findEntityGraphByUsername ===");
        List<Member> memberFetchJoin = memberRepository.findEntityGraphByUsername("member1");
        for (Member memberFetch : memberFetchJoin) {
            System.out.println("memberFetch.getUsername() = " + memberFetch.getUsername());
            // memberFetch.getTeam().getClass() = class study.datajpa.entity.Team
            // 프록시가 아닌 순수한 엔티티
            System.out.println("memberFetch.getTeam().getClass() = " + memberFetch.getTeam().getClass());
            System.out.println("memberFetch.getTeam().getName() = " + memberFetch.getTeam().getName());
        }
    }

    @Test
    public void queryHint() throws Exception {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); // 영속성 컨택스트에 Member가 아직 남아있다.
        em.clear();

        // when
        Member findMember = memberRepository.findById(member1.getId()).get();
        findMember.setUsername("member2");
        // dirty checking 동작 -> update 쿼리가 나간다
        // dirty checking을 하기위해서 -> 원본 데이터 & 변경된 데이터 : 총 2개의 데이터를 관리해야한다.
        // -> 메모리를 먹는다.
        // update는 안하고 100% 조회만 할경우 dirty checking에 필요한 메모리가 낭비된다.
        // -> 이를 최적화 하기 위해서 "JPA Hint"를 준다
        em.flush();

        // then

    }

    @Test
    public void queryHintFindReadOnlyByUsername() throws Exception {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); // 영속성 컨택스트에 Member가 아직 남아있다.
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        // findReadOnlyByUsername에 JPA Hint로 readOnly정보를 설정했다
        // -> dirty checking을 위한 스냅샷(원본 데이터)를 만들지 않는다.
        // -> 변경이 안된다고 가정하여 setUsername()을 해도 무시한다. (dirty checking이 안된다)
        findMember.setUsername("member2");
        em.flush();

        // then

    }


    @Test
    public void queryHintFindLockByUsername() throws Exception {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); // 영속성 컨택스트에 Member가 아직 남아있다.
        em.clear();

        // when
        List<Member> findMember = memberRepository.findLockByUsername(member1.getUsername());

        // then

    }

    /**
     * 사용자 정의 메서드 호출 코드
     */
    @Test
    public void callCustom() throws Exception {
        // given
        List<Member> memberCustom = memberRepository.findMemberCustom();
        // when

        // then

    }

    @Test
    public void projections() throws Exception {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        /*
         * select m.username from member m
         * where m.username="m1";
         */
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        Assertions.assertThat(result.size()).isEqualTo(1);

    }
}
