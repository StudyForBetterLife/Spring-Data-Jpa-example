package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * [XXRepository + Impl] 라는 명명 규칙을 맞추면
 * Spring Data JPA가 자동으로 커스텀 구현체를 찾아서 호출해준다.
 *
 * 항상 사용자 정의 리포지토리가 필요한 것은 아니다
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
