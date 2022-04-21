package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    @Embedded
    private Address address;

    //연관관계의 주인이 아닐 경우 mappedBy 입력
    //값을 넣어 직접 수정 불가
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
