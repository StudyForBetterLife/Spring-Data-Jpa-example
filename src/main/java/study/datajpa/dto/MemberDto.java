package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;


    /**
     * 엔티티는 DTO를 보면 안되지만
     * DTO는 엔티티를 봐도 괜찮다
     */
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
