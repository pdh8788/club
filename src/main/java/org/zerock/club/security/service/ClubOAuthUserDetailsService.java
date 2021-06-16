package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuthUserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    /**
     *
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     *
     * OAuth2UserService
     * 구글과 같은 서비스에서 로그인 처리가 끝난 결과를 가져오는 작업을 사용할 수 있는 환경을 구성하는 것입니다.
     * 이를 위해서는 실제 소셜 로그인 과정에서 동작하는 존재인 OAuth2UserService라는 것을 알아야 합니다.
     *
     * 이를 구현하는 것은 OAuth의 인증 결과를 처리한다는 의미입니다.
     *
     * 인터페이스를 직접 구현할 수도 있지만 좀 편하게 하고 싶다면 구현된 클래스 중에서 하나를 사용하는 방식이 더 편할 것이므로
     * DefualtOAuth2UserService클래스를 상속해서 구현합니다. security 패키지 내에 있는 service 패키지에
     * DefaultOAuth2UserService를 상속하는 클래스를 ClubOAuth2UserDetailsService라는 클래스로 구성합니다.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("-----------------------------------");
        log.info("userRequest: " + userRequest);

        /**
         * loadUser()는 OAuth2UserRequest라는 타입의 파라미터와 OAuth2User라는 타입의 리턴 타입을 반환합니다.
         * 문제는 기존의 로그인 처리에 사용하던 파라미터나 리턴 타입과는 다르기 때문에 이를 변환해서 처리해야만
         * 브라우저와 컨트롤러의 결과를 일반적인 로그인과 동일하게 사용할 수 있습니다.
         */

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName); // Google로 출력
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==================================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v);
        });

        String email = null;

        if( clientName.equals("Google")){//구글을 이용하는 경우
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL: " + email);

        /*
        ClubMember member = saveSocialMember(email);

        return oAuth2User;
        */

        ClubMember member = saveSocialMember(email);

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                   role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        clubAuthMember.setName(member.getName());

        return clubAuthMember;
    }

    private ClubMember saveSocialMember(String email) {

        //기존에 동일한 이메일로 가입한 회원의 있는 경우에는 그대로 조회만
        Optional<ClubMember> result = repository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        // 없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        ClubMember clubMember = ClubMember.builder().email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        repository.save(clubMember);

        return clubMember;
    }
}
