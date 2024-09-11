package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.simplequery.OrderSimpleQueryRepository;
import jpabook.jpashop.repository.simplequery.dto.OrderSimpleQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /*
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<Order> all = orderRepository.findAllbyString(orderSearch);

        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기환
        }

        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public Result orderV2() {

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");
        List<Order> all = orderRepository.findAllbyString(orderSearch);

        List<SimpleOrderDto> orderDtos = all.stream()
                .map(m -> new SimpleOrderDto(m))
                .collect(Collectors.toList());

        return new Result(orderDtos.size(), orderDtos);
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O) - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함) */
    @GetMapping("/api/v3/simple-orders")
    public Result orderV3() {

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");

        List<Order> finAllWithMemberDelivery = orderRepository.finAllWithMemberDelivery(orderSearch);

        return new Result(finAllWithMemberDelivery.size(), finAllWithMemberDelivery);
    }
    /**
     * V4. JPA에서 DTO로 바로 조회 - 쿼리1번 호출
     * select 절에서 원하는 데이터만 선택해서 조회
     *
     * DTO로 직접 조회 시 영속성 컨텍스트에서 관리되지 않는다.
     * 지정된 값만 가져오기 때문에 사용 용도가 명확해야 한다.(다른 로직에서 사용하기 어려움)
     * */
    @GetMapping("/api/v4/simple-orders")
    public Result orderV4() {

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("userA");

        List<OrderSimpleQueryDto> finAllWithMemberDelivery = orderSimpleQueryRepository.finAllWithMemberDeliveryDto();

        return new Result(finAllWithMemberDelivery.size(), finAllWithMemberDelivery);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        int count;
        T result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); // Lazy 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
}
