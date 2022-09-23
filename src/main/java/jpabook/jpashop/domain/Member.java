package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //연관관계의 주인이 아닐 경우 mappedBy 입력
    //값을 넣어 직접 수정 불가
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member() {

    }

    @Builder
    public Member(String name, Address address, List<Order> orders) {
        this.name = name;
        this.address = address;
        this.orders = orders;
    }
}
