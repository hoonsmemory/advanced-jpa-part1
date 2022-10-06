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

    //@Rollback(value = false)
    //@Transactional
    //@Test
    //@Before
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


        ContestTeam contestTeamA1 = ContestTeam.createContestTeam(teamA);
        entityManager.persist(contestTeamA1);

        ContestTeam contestTeamA2 = ContestTeam.createContestTeam(teamB);
        entityManager.persist(contestTeamA2);

        Contest contestA = Contest.createContest("contestA", promoterA, contestTeamA1, contestTeamA2);
        entityManager.persist(contestA);

        ContestTeam contestTeamB1 = ContestTeam.createContestTeam(teamC);
        entityManager.persist(contestTeamB1);
        ContestTeam contestTeamB2 = ContestTeam.createContestTeam(teamB);
        entityManager.persist(contestTeamB2);

        Contest contestB = Contest.createContest("contestB", promoterB, contestTeamB1, contestTeamB2);
        entityManager.persist(contestB);
    }

    @Test
    public void 조회() {

    }

}