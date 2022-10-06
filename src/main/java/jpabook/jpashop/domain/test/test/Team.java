package jpabook.jpashop.domain.test.test;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Team(String teamName) {
        this.teamName = teamName;
    }

    public void setMembers(List<Participant> participants) {
        this.participants = participants;
    }
}
