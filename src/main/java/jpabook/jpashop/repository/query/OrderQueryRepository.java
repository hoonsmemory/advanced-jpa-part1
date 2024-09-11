package jpabook.jpashop.repository.query;


import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 1번 (IN 절)
     */
    public List<OrderQueryDto> findOrderDtos(int offset, int limit) { //루트 조회(toOne 코드를 모두 한번에 조회)

        //1. Order 조회
        List<OrderQueryDto> result = findOrders(offset, limit); //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)

        //2. 조회된 키값 가져오기
        List<Long> orderIds = result.stream()
                        .map(o -> o.getOrderId())
                        .collect(Collectors.toList());

        //3. 조회된 키값을 대상으로 orderItem 조회 (in 절로 한번에 조회)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItems(orderIds);

        //4. 루프를 돌면서 컬렉션 추가
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }


    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N 번 * 단건 조회에서 많이 사용하는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos(int offset, int limit) { //루트 조회(toOne 코드를 모두 한번에 조회)

        List<OrderQueryDto> result = findOrders(offset, limit); //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }


    /**
     * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     */
    private List<OrderQueryDto> findOrders(int offset, int limit) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderQueryDto(o.id, m.name, o.orderDate,o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId",
                        OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    /**
     * 1:N 관계인 orderItems 조회
     * In절 사용
     */
    private Map<Long, List<OrderItemQueryDto>> findOrderItems(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds",
                        OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    public List<OrderFlatDto> findOrderDtos_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}

