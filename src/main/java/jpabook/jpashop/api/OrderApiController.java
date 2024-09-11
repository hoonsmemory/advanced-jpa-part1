package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderQueryDto;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<Order> all = orderRepository.findAllbyString(orderSearch);

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream()
                    .forEach(o -> o.getItem().getName());
        }

        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 트랜잭션 안에서 지연 로딩 필요
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<Order> orders = orderRepository.findAllbyString(orderSearch);

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능)
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<OrderDto> orderDtos = orderRepository.findAllWithItem()
                .stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return orderDtos;
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * -ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3_1/orders")
    public List<OrderDto> ordersV3_1(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     @RequestParam(value = "limit", defaultValue = "100") int limit) {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");

        // 1. -ToOne 관계만 우선 모두 페치 조인으로 최적화
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        // 2. hibernate.default_batch_fetch_size 를 사용해 최적화
        List<OrderDto> orderDtos = orders.stream()
                                         .map(o -> new OrderDto(o))
                                         .collect(Collectors.toList());

        return orderDtos;
    }

    /**
     *V4.JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + NQuery)
     * - 페이징 가능
     * - OrderId 수만큼 OrderItem을 가져오는 문제가 발생
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos(offset, limit);
        return orderQueryDtos;
    }

    /**
     *V5.JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * - 페이징 가능
     * - In절을 이용해서 해결
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos(offset, limit);
        return orderQueryDtos;
    }


    /**
     * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
     * - 페이징 불가능... *
     */

    @Data
    private class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItem;


        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItem = order.getOrderItems().stream()
                    .map( o -> new OrderItemDto(o))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
