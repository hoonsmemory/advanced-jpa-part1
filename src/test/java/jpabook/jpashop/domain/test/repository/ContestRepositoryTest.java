package jpabook.jpashop.domain.test.repository;

import jpabook.jpashop.domain.test.test.Contest;
import jpabook.jpashop.domain.test.test.ContestTeam;
import jpabook.jpashop.domain.test.test.Team;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContestRepositoryTest {

    @Autowired
    ContestRepository contestRepository;

    @Transactional
    @Test
    public void v1() {
        List<Contest> result = contestRepository.findContestV1();
        List<SimpleContestDto> contestDtos = result.stream()
                .map(c -> new SimpleContestDto(c))
                .collect(Collectors.toList());

        for (SimpleContestDto contestDto : contestDtos) {
            System.out.println(contestDto);
        }
    }

    @Transactional
    @Test
    public void v2() {
        List<Contest> result = contestRepository.findContestV2();
        List<SimpleContestDto> contestDtos = result.stream()
                .map(c -> new SimpleContestDto(c))
                .collect(Collectors.toList());

        for (SimpleContestDto contestDto : contestDtos) {
            System.out.println(contestDto);
        }
    }

    @Transactional
    @Test
    public void v3() {
        List<Contest> result = contestRepository.findContestV3();


        List<ContestDto> contestDtos = result.stream()
                .map(c -> new ContestDto(c))
                .collect(Collectors.toList());

        for (ContestDto contestDto : contestDtos) {
            System.out.println(contestDto);
        }
    }

    @Transactional
    @Test
    public void v4() {

        int offset = 0;
        int limit = 2;
        List<Contest> result = contestRepository.findContestV4(offset, limit);


        List<ContestDto> contestDtos = result.stream()
                .map(c -> new ContestDto(c))
                .collect(Collectors.toList());

        for (ContestDto contestDto : contestDtos) {
            System.out.println(contestDto);
        }
    }



    @Data
    static class SimpleContestDto {
        private Long contestId;
        private String contestName;
        private String promoterName;

        public SimpleContestDto(Contest contest) {
            this.contestId = contest.getId();
            this.contestName = contest.getContestName();
            this.promoterName = contest.getPromoter().getPromoterName();
        }

        public Long getContestId() {
            return contestId;
        }

        public String getContestName() {
            return contestName;
        }

        public String getPromoterName() {
            return promoterName;
        }

        @Override
        public String toString() {
            return "SimpleContestDto{" +
                    "contestId=" + contestId +
                    ", contestName='" + contestName + '\'' +
                    ", promoterName='" + promoterName + '\'' +
                    '}';
        }
    }

    static class ContestDto {
        private Long contestId;
        private String contestName;
        private String promoterName;

        private List<ContestTeamDto> contestTeamDtos;

        public ContestDto(Contest contest) {
            this.contestId = contest.getId();
            this.contestName = contest.getContestName();
            this.promoterName = contest.getPromoter().getPromoterName();
            this.contestTeamDtos = contest.getContestTeams().stream()
                    .map(c -> new ContestTeamDto(c))
                    .collect(Collectors.toList());
        }

        public Long getContestId() {
            return contestId;
        }

        public String getContestName() {
            return contestName;
        }

        public String getPromoterName() {
            return promoterName;
        }

        public List<ContestTeamDto> getContestTeamDtos() {
            return contestTeamDtos;
        }

        @Override
        public String toString() {
            return "ContestDto{" +
                    "contestId=" + contestId +
                    ", contestName='" + contestName + '\'' +
                    ", promoterName='" + promoterName + '\'' +
                    ", contestTeamDtos=" + contestTeamDtos +
                    '}';
        }
    }

    static class ContestTeamDto {
        private Long contestTeamId;
        private Integer rank;
        private String teamName;

        public Long getContestTeamId() {
            return contestTeamId;
        }

        public Integer getRank() {
            return rank;
        }

        public String getTeamName() {
            return teamName;
        }

        public ContestTeamDto(ContestTeam contestTeam) {
            this.contestTeamId = contestTeam.getId();
            this.rank = contestTeam.getRank();
            this.teamName = contestTeam.getTeam().getTeamName();
        }

        @Override
        public String toString() {
            return "ContestTeamDto{" +
                    "contestTeamId=" + contestTeamId +
                    ", rank=" + rank +
                    ", teamName='" + teamName + '\'' +
                    '}';
        }
    }


}