package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // JUnit 실행 시 스프링을 엮어서 실행하고 싶을 때 적용
@SpringBootTest //스프링부트를 실행한 상태에서 테스트가 필요할 때 적용(없을 경우 @Autowired 에러 발생)
@Transactional // test의 경우 자동 롤백을 한다.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("")
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("이성훈");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    @DisplayName("")
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("lee");

        Member member2 = new Member();
        member2.setName("lee");

        // when
        memberService.join(member1);
        memberService.join(member2);

        // then
        fail("예외가 발생되야 합니다.");
    }
}