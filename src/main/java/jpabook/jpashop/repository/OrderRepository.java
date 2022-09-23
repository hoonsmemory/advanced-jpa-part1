package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllbyString(OrderSearch orderSearch) {
        List<Order> orderlist = em.createQuery("select o " +
                                                        "from Order o " +
                                                        "join o.member m " +
                                                        "where o.status = :status " +
                                                        "and m.name like :name", Order.class)
                                  .setParameter("status",orderSearch.getOrderStatus())
                                  .setParameter("name",orderSearch.getMemberName())
                                  .setMaxResults(1000) //최대 1000건
                .getResultStream().collect(Collectors.toList());
        return orderlist;
    }

    public List<Order> finAllWithMemberDelivery(OrderSearch orderSearch) {

        List<Order> finAllWithMemberDelivery = em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "where o.status = :status"
                , Order.class
        ).setParameter("status", orderSearch.getOrderStatus())
                .getResultList();

        return finAllWithMemberDelivery;
    }
}
