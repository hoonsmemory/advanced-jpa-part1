package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문")
    public void 상품주문() throws Exception
    {
        // given
        Member member = createMember();
        Item item = createItem("수학책", 10000, 100);

        // when
        int count = 10;
        Long orderId = orderService.order(member.getId(), item.getId(), count);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 100 - count, item.getStockQuantity());
        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * count, getOrder.getTotalPrice());

    }



    @Test(expected = NotEnoughStockException.class)
    @DisplayName("상품주문_재고수량초과")
    public void 상품주문_재고수량초과() throws Exception
    {
        // given
        Member member = createMember();
        Item item = createItem("수학책", 10000, 100);

        // when
        int count = 1000;
        Long orderId = orderService.order(member.getId(), item.getId(), count);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("주문 취소")
    public void 주문취소() throws Exception
    {
        // given
        Member member = createMember();
        Item item = createItem("인프런 JPA 강의", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL,order.getStatus());
        assertEquals("주문 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("이성훈");
        member.setAddress(new Address("서울", "테헤란로", "12345"));
        em.persist(member);
        return member;
    }

    private Item createItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }

}