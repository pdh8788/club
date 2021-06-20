package org.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

    /**
     * ClubAuthMemberDTO를 구성하는 첫 번째 단계는 User 클래스를 상속하고 부모 클래스인 User 클래스의 생성자를
     * 호출할 수 있는 코드를 만드는 것입니다.( 부모 클래스인 User클래스에 사용자가 정의 생성자가 있으므로 반드시 호출할 필요가 있습니다.)
     *
     * ClubAuthMemberDTO는 DTO 역할을 수행하는 클래스인 동시에 스프링 시큐르티에서 인가/인증 작업에 사용할 수 있습니다.
     */
    public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities, Map<String,Object> attr ) {
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    /**
     * OAuth2User타입을 ClubAuthMemberDTO타입으로 사용할 수 있도록 처리해 줄 필요가 있습니다.
     */

    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    private Map<String, Object> attr;

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
    }

    /**
     *
     * @return
     * OAuth2User는 Map 타입으로 모든 인증 결과를 attributes라는 이름으로 가지고 있기 때문에
     * ClubAuthMember 역시 attr이라는 변수를 만들어주고 getAttributes()메서드를 override한 점입니다.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
