package SpringDataJpa.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)      // 팀하고 다대일 관계 -> '다' 쪽인 member에 포린키를 둔다
    @JoinColumn(name = "team_id")   // 포린키 이름이 team_id
    private Team team;

//    protected Member() {
//    }

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

    // 연관관계 변경하는 메소드 (멤버가 팀을 변경)
    public void changeTeam(Team team) {
        this.team = team;               // 팀을 변경
        team.getMembers().add(this);    // 그 팀에 가서도 멤버를 추가
    }


}
