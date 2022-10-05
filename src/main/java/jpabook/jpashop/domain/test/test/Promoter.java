package jpabook.jpashop.domain.test.test;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Promoter {

    @Id
    @GeneratedValue
    @Column(name = "promoter_id")
    private Long id;

    @Column(name = "promoter_name")
    private String promoterName;

    @OneToOne(mappedBy = "promoter", fetch = FetchType.LAZY)
    private Contest contest;

    @Builder
    public Promoter(String promoterName) {
        this.promoterName = promoterName;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
