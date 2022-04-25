package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 생성자 인젝션
     * 생성자가 하나만 있을 경우 @Autowired를 사용하지 않아도 알아서 인젝션 해준다.
     * 테스트에 Mock을 넣을 수 있어 테스트에 용이하다.
     * 변경하지 않기 때문에 final을 적어준다.
     * 아래와 같이 직접 생성자를 만들어줘도 되지만 @RequiredArgsConstructor를 사용해서 소스를 줄일 수 있다.
     */
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 단건 조회
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    //회원 전체 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
