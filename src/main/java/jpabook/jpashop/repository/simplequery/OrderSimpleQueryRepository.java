package jpabook.jpashop.repository.simplequery;


import jpabook.jpashop.repository.simplequery.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> finAllWithMemberDeliveryDto() {

        List<OrderSimpleQueryDto> finAllWithMemberDelivery = em.createQuery(
                "select new jpabook.jpashop.repository.simplequery.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d "
                , OrderSimpleQueryDto.class
        ).getResultList();

        return finAllWithMemberDelivery;
    }
}
