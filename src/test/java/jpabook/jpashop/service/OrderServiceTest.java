package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
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
        Member member = new Member();
        member.setName("이성훈");
        member.setAddress(new Address("서울","테헤란로","12345"));
        em.persist(member);

        Item item = new Book();
        item.setName("수학책");
        item.setPrice(10000);
        item.setStockQuantity(100);
        em.persist(item);

        // when
        int count = 10;
        Long orderId = orderService.order(member.getId(), item.getId(), count);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", OrderStatus.ORDER, getOrder.getStatus());
    }
    
    @Test
    @DisplayName("주문 취소")
    public void 주문취소() throws Exception
    {
        // given
        
        // when
        
        // then
    }
    
    @Test
    @DisplayName("상품주문_재고수량초과")
    public void 상품주문_재고수량초과() throws Exception
    {
        // given
        
        // when
        
        // then
    }
}