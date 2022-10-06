package jpabook.jpashop.domain.test.test;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Contest {

    @Id @GeneratedValue
    @Column(name = "contest_id")
    private Long id;

    @Column(name = "contest_name")
    private String ContestName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "promoter_id")
    Promoter promoter;

    @OneToMany(mappedBy = "contest")
    List<ContestTeam> contestTeams = new ArrayList<>();

    public void addPromoter(Promoter promoter) {
        this.promoter = promoter;
        promoter.setContest(this);
    }

    public void setContestName(String contestName) {
        ContestName = contestName;
    }

    public void addContestTeam(ContestTeam contestTeam) {
        this.contestTeams.add(contestTeam);
        contestTeam.setContest(this);
    }

    public static Contest createContest(String contestName, Promoter promoter, ContestTeam... contestHistories) {
        Contest contest = new Contest();
        contest.setContestName(contestName);
        contest.addPromoter(promoter);

        for (ContestTeam contestTeam : contestHistories) {
            contestTeam.updateRank(1);
            contest.addContestTeam(contestTeam);
        }

        return contest;
    }
}
