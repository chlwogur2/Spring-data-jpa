package SpringDataJpa.datajpa.dto;

import SpringDataJpa.datajpa.entity.Member;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    // 모든 argument 가 들어간 생성자가 필요
    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    // DTO 가 엔티티를 바라보게 하면 만들 때 좀 쉽다
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
