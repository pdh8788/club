package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ClubMember가 ClubAuthMemberDTO라는 타입으로 처리된 가장 큰 이유는 사용자의 정보를 가져오는 핵심적인 역할을 하는
 * UserDetailService라는 인터페이스 때문입니다. 시큐리트 구조에서 인증을 담당하는 AuthenticationManager는 내부적으로
 * UserDetaiService를 호출해서 사용자의 정보를 가져옵니다. 현재 예제와 같이 JPA로 사용자의 정보를 가져오고 싶다면
 * UserDetailService가 이용하는 구조로 작성할 필요가 있습니다.
 *
 * @Service를 사용해서 자동으로 스프링에서 빈으로 처리될 수 있게 했다는점.
 *  빈으로 등록되면 이를 자동으로 스프링 스큐리티에서 UserDetailService로 인식하기 때문에 기존에 임시로 코드로 작성된
 *  Security configure 부분을 사용하지 않도록 설정해야 합니다.
 */
@Log4j2
@Service
@RequiredArgsConstructor // ClubMemberRepositoru를 주입받을 수 있도록 어노테이션 선언
public class ClubUserDetailService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    /**
     *
     * ---- UserDetails 인터페이스
     * loadUserByUsername() 말 그대로 username이라는 회원 아이디와 같은 식별 값으로 회원 정보를 가져옵니다.
     * 메서드의 리턴 타입은 UserDetail이라는 타입인데 이를 통해서 다음과 같은 정보를 알아낼 수 있습니다.
     * 
     * getAuthorities() - 사용자가 가지는 권한에 대한 정보
     * getPassword() - 인증을 마무리하기 위한 패스워드 정보
     * getUsername() - 인증에 필요한 아이디와 같은 정보
     * 계정 만료 여부 - 더이상 사용이 불가능한 계정인지 알 수 있는 정보
     * 계정 잠김 여부 - 현재 계정의 잠김 여부
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailService loadUserByUsername " + username);

        // userName이 실제로는 ClubMember에서 email을 의미
        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();

        log.info("----------------------------------------------");
        log.info(clubMember);

        /**
         * ClubMember를 처리할 수 있도록 DTO와 같은 개념으로 별도의 클래스를 구성하고
         * 이를 활용하는 방법
         */
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
