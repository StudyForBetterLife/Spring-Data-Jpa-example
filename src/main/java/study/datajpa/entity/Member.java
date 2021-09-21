package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

/**
 * protected Member() {
 * }
 * -> @NoArgsConstructor(access = AccessLevel.PROTECTED) 으로 대체
 * <p>
 * [JPA NamedQuery]
 *
 * @NamedQuery( name = "Member.findByUsername",
 * query = "select m from Member m where m.username = :username"
 * )
 * 1. MemberJpaRepository 에서 findByUsername 메소드를 구현한다.
 * (em.createNamedQuery("Member.findByUsername", Member.class))
 * 2. MemberRepository 에서 인터페이스를 작성한다
 * (List<Member> findByUsername(@Param("username") String username);)
 * <p>
 * NamedQuery의 장점
 * -> JPQL 문법이 잘못되면 어플리케이션 로딩 시점에 에러를 발생시킨다. (오타를 잡을 수 있다)
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) // ToString 메소드에서 출력할 변수 설정
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
//@NamedEntityGraph(
//        name = "Member.all",
//        attributeNodes = @NamedAttributeNode("team")
//)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}


