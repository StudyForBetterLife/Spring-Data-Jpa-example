package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import static org.assertj.core.api.Assertions.*;


/**
 * JUnit5 부터 @RunWith(SpringRunner.class) 어노테이션이 필요 없다
 *
 * 쿼리를 보기 위해 @Rollback(value = false) 설정 (실무에서는 제거해라)
 */
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() throws Exception {
        // given
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        // when
        Member findMember = memberJpaRepository.find(savedMember.getId());
        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

}