package jpabook.jpashop.domain.test.test;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ContestTeam {

    @Id @GeneratedValue
    @Column(name = "Contest_Team_id")
    private Long id;

    private Integer rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    Team team;

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public static ContestTeam createContestTeam(Team team) {
        ContestTeam contestTeam = new ContestTeam();
        contestTeam.setTeam(team);

        return contestTeam;
    }

    public void updateRank(Integer rank) {
        this.rank = rank;
    }

}
