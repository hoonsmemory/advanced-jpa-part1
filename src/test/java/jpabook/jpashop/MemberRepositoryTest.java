package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false) //일반적으로 test 환경에서 Transactional 어노테이션만 있을 경우 실행 후 rollback 처리한다.
    public void test() throws Exception {

        Member member = new Member();
        member.setUsername("이성훈");

        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        /**
         * 같은 트랜잭션 안에서 저장, 조회가 일어나면 영속성 컨텍스트에 동일한 값이 들어있다.
         * 아래 비교한 member는 같은 ID를 가지고 있으므로 동일한 객체가 된다.
         * (저장할 때 영속성 컨텍스트에 1차 캐시로 남아 있기 때문에 조회 쿼리없이 해당 값을 가져온다.)
         */
        System.out.println("findMember == member : " + (findMember == member));
    }
}