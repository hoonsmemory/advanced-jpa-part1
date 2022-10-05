package jpabook.jpashop.domain.test;

import jpabook.jpashop.domain.test.test.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
class ContestServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Rollback(value = false)
    @Transactional
    @Test
    public void setting() {

        Team teamA = Team.builder()
                .teamName("teamA")
                .build();
        entityManager.persist(teamA);


        Team teamB = Team.builder()
                .teamName("teamB")
                .build();
        entityManager.persist(teamB);


        Team teamC = Team.builder()
                .teamName("teamC")
                .build();
        entityManager.persist(teamC);


        for(int i = 1; i < 7; i++) {
            Participant member = Participant.builder()
                    .username("member"+i)
                    .build();
            if((i % 3) == 0) {
                member.addTeam(teamC);
            } else if((i % 2) == 0) {
                member.addTeam(teamB);
            } else {
                member.addTeam(teamA);
            }

            entityManager.persist(member);
        }

        Promoter promoterA = Promoter.builder()
                .promoterName("promoterA")
                .build();
        entityManager.persist(promoterA);


        Promoter promoterB = Promoter.builder()
                .promoterName("promoterB")
                .build();
        entityManager.persist(promoterB);


        ContestHistory contestHistoryA1 = ContestHistory.createContestHistory(teamA);
        entityManager.persist(contestHistoryA1);

        ContestHistory contestHistoryA2 = ContestHistory.createContestHistory(teamB);
        entityManager.persist(contestHistoryA2);

        Contest contestA = Contest.createContest("contestA", promoterA, contestHistoryA1, contestHistoryA2);
        entityManager.persist(contestA);

        ContestHistory contestHistoryB1 = ContestHistory.createContestHistory(teamC);
        entityManager.persist(contestHistoryB1);
        ContestHistory contestHistoryB2 = ContestHistory.createContestHistory(teamB);
        entityManager.persist(contestHistoryB2);

        Contest contestB = Contest.createContest("contestB", promoterB, contestHistoryB1, contestHistoryB2);
        entityManager.persist(contestB);
    }

}