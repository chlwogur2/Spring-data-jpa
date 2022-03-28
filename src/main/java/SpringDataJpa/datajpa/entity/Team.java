package SpringDataJpa.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
public class Team extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")   // 포린키가 없는 쪽에 mappedBy 걺,
                                    // 포린키 있는 쪽(Member) 의 team 에 의해 매핑이 된다
    private List<Member> members = new ArrayList<>();

//    protected Team() {
//    }

    // 생성자

    public Team(String name) {
        this.name = name;
    }
}
