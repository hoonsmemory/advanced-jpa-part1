package jpabook.jpashop.api;

import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 리턴값이 JsonArray 일 경우 한번 감싸서 리턴
     *
     */
    @GetMapping("/api/members")
    public Result getMembers() {
        List<MemberDto> allMembers = memberService.findAll()
                .stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(allMembers.size(), allMembers);
    }
    
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private Integer count;
        private T result;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }
}
