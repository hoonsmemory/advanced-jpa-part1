package jpabook.jpashop.repository.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {

        private Long orderId;
        private String itemName;
        private int orderPrice;
        private int count;
}
