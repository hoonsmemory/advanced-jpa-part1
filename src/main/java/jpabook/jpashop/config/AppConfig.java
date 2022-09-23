가package jpabook.jpashop.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {

    @Bean
    Hibernate5Module hibernate5Module() {
        //기본적으로 초기화 된 프록시 객체만 노출, 초기화 되지 않은 프록시 객체는 노출 안함
        Hibernate5Module hibernate5Module = new Hibernate5Module();

        //강제 지연 로딩 설정
        //hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);

        return hibernate5Module;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            private EntityManager entityManager;

            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {

                initData1();
                initData2();
            }

            private void initData1() {
                Member member1 = new Member();
                member1.setName("userA");
                member1.setAddress(new Address("서울", "테헤란로", "1234"));
                entityManager.persist(member1);

                Book book1 = new Book();
                book1.setName("JPA1 BOOK");
                book1.setPrice(10000);
                book1.setStockQuantity(100);
                entityManager.persist(book1);

                Book book2 = new Book();
                book2.setName("JPA2 BOOK");
                book2.setPrice(20000);
                book2.setStockQuantity(100);
                entityManager.persist(book2);

                OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
                OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

                Delivery delivery = new Delivery();
                delivery.setAddress(member1.getAddress());

                Order order = Order.createOrder(member1, delivery, orderItem1, orderItem2 );
                entityManager.persist(order);
            }

            private void initData2() {
                Member member1 = new Member();
                member1.setName("userB");
                member1.setAddress(new Address("인천", "소래포구", "4321"));
                entityManager.persist(member1);

                Book book1 = new Book();
                book1.setName("Spring1 BOOK");
                book1.setPrice(20000);
                book1.setStockQuantity(200);
                entityManager.persist(book1);

                Book book2 = new Book();
                book2.setName("Spring2 BOOK");
                book2.setPrice(40000);
                book2.setStockQuantity(300);
                entityManager.persist(book2);

                OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 3);
                OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 4);

                Delivery delivery = new Delivery();
                delivery.setAddress(member1.getAddress());

                Order order = Order.createOrder(member1, delivery, orderItem1, orderItem2 );
                entityManager.persist(order);
            }
        };

    }
}
