package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

//    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll(){
        log.info("exAll............");
    }

    /**
     * 
     * @param clubAuthMemberDTO
     * 컨트롤러에서 로그인된 사용자 정보를 확인하는 방법은 크게 2가지 방식이 있습니다.
     * 1)SeucyrityContextHolder라는 객체를 사용하는 방법.
     * 2)직접 파라미터와 어노테이션을 사용하는 방식
     */
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO)
    {
        log.info("exMember.........");

        log.info("-----------------------------");
        log.info(clubAuthMemberDTO);
    }

//    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user95@zerock.org\"")
    @GetMapping("/exOnly")
    public void exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember)
    {
        log.info("exMemberOnly.........");
        log.info(clubAuthMember);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin.........");
    }
}
