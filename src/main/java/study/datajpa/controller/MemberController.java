package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

//    @PostConstruct
//    public void init() {
//        for (int i = 0; i < 100; i++) {
//            memberRepository.save(new Member("user" + i, i));
//        }
//    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * HTTP 요청은 회원 id 를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환한다.
     * 도메인 클래스 컨버터도 리파지토리를 사용해서 엔티티를 찾는다.
     * 미쳤따리
     * <p>
     * 단, 오직 조회용으로만 사용해야 한다.
     * 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용해야 한다.
     * (트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다.)
     * <p>
     * 권장하지 않는다.
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername(); // member 값을 변경해도 DB에 반영되지 않는다.
    }


    /**
     * Web 확장 - 페이징과 정렬
     * <p>
     * [요청 파라미터]
     * "/members?page=0&size=3&sort=id,desc&sort=username,desc"
     * - page: 현재 페이지, 0부터 시작한다.
     * - size: 한 페이지에 노출할 데이터 건수
     * - sort: 정렬 조건을 정의한다. 예) 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort
     * 파라미터 추가 ( asc 생략 가능)
     * <p>
     * [글로벌 설정]
     * spring.data.web.pageable.default-page-size=20 /# 기본 페이지 사이즈/
     * spring.data.web.pageable.max-page-size=2000 /# 최대 페이지 사이즈/
     * <p>
     * [개별 설정]
     *
     * @PageableDefault 어노테이션을 사용하면 된다.
     * 우선순위가 글로벌 설정보다 높다.
     */
    @GetMapping("/members")
    public Page<Member> list(
            @PageableDefault(size = 12, sort = "username",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * [접두사]
     * 페이징 정보가 둘 이상이면 접두사로 구분한다
     *
     * @Qualifier 에 접두사명 추가 "{접두사명}_xxx”
     * <p>
     * [요청 파라미터]
     * /members?member_page=0&order_page=1
     */
    @GetMapping("/members2")
    public Page<Member> list2
    (@Qualifier("member") Pageable memberPageable,
     @Qualifier("order") Pageable orderPageable) {
        return memberRepository.findAll(memberPageable);
    }

    /**
     * Page 내용을 DTO로 변환하기
     */
    @GetMapping("/members3")
    public Page<MemberDto> list3(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> pageDto = page.map(MemberDto::new);
        return pageDto;
    }
}
