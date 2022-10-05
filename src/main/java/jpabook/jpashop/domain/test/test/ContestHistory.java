package jpabook.jpashop.domain.test.test;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ContestHistory {

    @Id @GeneratedValue
    @Column(name = "Contest_History_id")
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

    public static ContestHistory createContestHistory(Team team) {
        ContestHistory contestHistory = new ContestHistory();
        contestHistory.setTeam(team);

        return contestHistory;
    }

    public void updateRank(Integer rank) {
        this.rank = rank;
    }

}
