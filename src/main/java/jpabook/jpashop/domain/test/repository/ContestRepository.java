package jpabook.jpashop.domain.test.repository;

import jpabook.jpashop.domain.test.test.Contest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ContestRepository {

    private final EntityManager em;


    public List<Contest> findContestV1() {
        List<Contest> result = em.createQuery(
                "select c " +
                        "from Contest c " +
                        "join c.promoter p", Contest.class
        ).getResultList();

        return result;
    }

    public List<Contest> findContestV2() {
        List<Contest> result = em.createQuery(
                "select c " +
                        "from Contest c " +
                        "join FETCH c.promoter p", Contest.class
        ).getResultList();

        return result;
    }

    public List<Contest> findContestV3() {
        List<Contest> result = em.createQuery(
                "select distinct c " +
                        "from Contest c " +
                        "join FETCH c.promoter p " +
                        "join FETCH c.contestTeams ct " +
                        "join FETCH ct.team t", Contest.class
        ).getResultList();

        return result;
    }

    public List<Contest> findContestV4(int offset, int limit) {
        List<Contest> result = em.createQuery(
                "select c " +
                        "from Contest c " +
                        "join FETCH c.promoter p", Contest.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return result;
    }

}
