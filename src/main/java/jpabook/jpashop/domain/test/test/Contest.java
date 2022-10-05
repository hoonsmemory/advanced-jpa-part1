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
    List<ContestHistory> contestHistories = new ArrayList<>();

    public void addPromoter(Promoter promoter) {
        this.promoter = promoter;
        promoter.setContest(this);
    }

    public void setContestName(String contestName) {
        ContestName = contestName;
    }

    public void addContestHistory(ContestHistory contestHistory) {
        this.contestHistories.add(contestHistory);
        contestHistory.setContest(this);
    }

    public static Contest createContest(String contestName, Promoter promoter, ContestHistory... contestHistories) {
        Contest contest = new Contest();
        contest.setContestName(contestName);
        contest.addPromoter(promoter);

        for (ContestHistory contestHistory : contestHistories) {
            contestHistory.updateRank(1);
            contest.addContestHistory(contestHistory);
        }

        return contest;
    }
}
