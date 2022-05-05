package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());


        /**
         * 도메인 모델 패턴 : 비즈니스 로직이 대부분 엔티티 안에 있다.
         *                 서비스계층서는 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
         *
         * 반대 : 트랜잭션 스크립트 패턴
         */
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        /**
         * orderItems 컬렉션, delivery 엔티티  CascadeType.ALL 옵션을 사용했기 때문에
         * order만 persist 하여도 전체 저장이 된다.
         *
         * CascadeType.ALL 범위 : orderItems 컬렉션, delivery 엔티티는 order 안에서만 쓰고,
         *                       다른 곳에서 가져다 쓰지 않기 때문에 사용 가능 (persist 라이프 사이클이 동일하다.)
         *
         */
        orderRepository.save(order);

        return order.getId();
    }


    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        /**
         *  엔티티안에 있는 데이터만 바꿔주면 더티체킹(변경내역 감지)을 통해 변경내역을 알아서 업데이트 해준다.
         *  JPA의 엄청 큰 장점
         */
        order.cancel();
    }

    /**
     *  검색
     */

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllbyString(orderSearch);
    }

}
