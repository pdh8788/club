package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailHandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.service.ClubUserDetailService;
import org.zerock.club.util.JWTUtil;

/**
 * 스프링 시큐리티를 이용하는 모든 프로젝트는 프로젝트에 맞는 설정을 추가하는 것이 일반적이므로
 * 이를 위한 별도의 시큐리티 설정 클래스를 사용하는 것이 일반적입니다.
 */
@Configuration
@Log4j2
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * @EnableGlobalMethodSecurity : 어노테이션 기반의 접근 제한을 설정할 수 있도록 하는 설정.
     * 일반적으로 Security Config와 같이 시큐리티 관련 설정 클래스에 붙이는 것이 일반적.
     * -> 접근 제한 어노테이션
     */

    @Autowired
    private ClubUserDetailService userDetailService ; // 주입

    /**
     * 스프링 시큐리티 내부에는 여러 개의 필터가 Filter Chaine이라는 구조로 Request를 처리하게 됩니다.
     * 개발 시에 필터를 확장하고 설정하면 스프링 시큐리티를 이용해서 다양한 형태의 로그인 처리가 가능하게 됩니다.
     *
     * ** 인증을 위한 AuthenticationManager
     * 필터의 핵심적인 동작은 AuthenticationManager를 통해서 인증이라는 타입의 객체로 작업을 하게 됩니다.
     *
     * 인증(Authentication)을 쉽게 이해하려면 '주민등록증'과 비슷하다고 생각하면 됩니다.
     * 예를 들어 로그인하는 과정에서는 사용자의 아이디/패스워드로 자신이 어떤 사람인지를 전달합니다.
     * 전달된 아이디/패스워드로 실제 사용자에 대해서 검증하는 행위는 AuthenticationManager(인증 매니저)를 통해서
     * 이루어 집니다.
     *
     * 실제 동작에서 전달되는 파라미터는 UserNamePasswordAuthenticationToken과 같이 토큰이라는
     * 이름으로 전달됩니다. 이 사실이 의미하는 바는 스프링 시큐리티 필터의 주요 역할이
     * 인증 관련된 정보를 토큰이라는 객체로 만들어서 전달하는 의미입니다.
     *
     * ** 인가(Authorization)와 권한/접근 제한
     * 인증처리 단계가 끝나면 다음으로 동작하는 부분은 '사용자의 권한이 적절한가?'에 대한 처리입니다.
     * 인가 = 승인 , 인증 = 사용자 자신의 증명
     */

    // 반드시 필요한 PasswordEncoder
    /**
     * 스프링부트 2.0 부터는 인증을 위해서 반드시 PasswordEncoder를 지정해야만 합니다.
     * Password Encoder중에 가장 많이 사용하는 것은 BCryptPasswordEncoder라는 클래스
     * BCryptPasswordEncoder로 암호화된 패스워드는 다시 원래대로 복호화가 불가능하고 매번 암호화된 값도 다르게
     * 됩니다. 대신에 특정한 문자열이 암호화된 결과인지만 확인할 수 있기 때문에 원본 내용을 알 수가 없습니다.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 설정
    /**
     * 암호화된 패스워드를 이용하기 위해서는 해당 암호를 사용하는 사용자가 필요합니다.
     * 이를 위해서 SecurityConfig에는 AuthenticationManager의 설정을 쉽게 처리할 수 있도록
     * 도와주는 configure() 메서드를 override해서 처리합니다.
     * AuthenticationManagerBuilder는 말 그대로 코드를 통해서 직접 인증 메니저를 설정할 때
     * 사용합니다.
     */
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 사용자 계정은 user1

        auth.inMemoryAuthentication().withUser("user1")
                .password("$2a$10$aQmUFdOnARYcI577VZbmk.5TRetGOdMP8YQs4YZd9J2X03ViWkmxu")
                .roles("USER");


    }
     */

    // 인가(Authorization)가 필요한 리소스 설정
    /**
     * 스프링 시큐리티를 이용해서 특정한 리소스에 접근 제한을 하는 방식은 크게
     * 1) 설정을 통해서 패턴을 지정하거나
     * 2) 이노테이션을 이용해서 적용하는 방법
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

       http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        // 인가/인증 절차에서 문제가 발생했을 때 로그인 페이지를 보여주도록 지정할 수 있고,
        // 화면으로 로그인 방식을 지원한다는 의미로 사용됩니다.
        // formLogin()을 이용하는 경우 별도의 디자인을 적용하기 위해서는 추가적인 설정이 필요합니다.
        // loginPage()나 loginProcessUrl(), defaultSuccessUrl(), faillureUrl() 등을 이용해서 필요한 설정을 지정할 수 있습니다.
        // 대부분의 애플리케이션은 고유한 디자인을 적용하기 때문에 loginPage()를 사용해서 별도의 로그인 페이지를 이용하는 경우가 많습니다.
        http.formLogin();

        // csrf토큰 비활성화
        http.csrf().disable();

        // logout 설정
        // formLogin()과 마찬가지로 logout() 메서드를 이용하면 로그아웃 처리가 가능합니다.
        // formLogout() 역시 로그인과 마찬가지로 별도의 설정이 없는 경우에는 스프링 시큐리티가 제공하는 웹 페이지를 보게 됩니다.
        // logOut에서 주의해야 할점은 CSRF토큰을 사용할 때는 반드시 POST 방식으로만 로그아웃을 처리해야만 합니다.
        // 반면에 CSRF토큰을 비활성화 시키면 GET(/logout)으로도 로그아웃이 처리 됩니다.
        // 로그아웃도 formLogin()과 마찬가지로 사용자가 별도의 로그아웃 관련 설정을 추가할 수 있습니다.
        http.logout();


        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailService); //7days

        //필터의 위치 조절
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    // CSRF 설정
    /**
     * 스프링 시큐리티는 기본적으로 CSRF(Cross Site Request Foregery - 크로스 사이트 요청 위조)
     * 라는 공격을 방어하기 위해서 임의의 값을 만들어서 이를 Get방식을 제외한 모든 요청 방식 등에
     * 포함시켜야만 정상적인 동작이 가능합니다.
     *
     * CSRF 공격은 '사이트간 요청 위조'라고 번역할 수 있습니다.
     * 서버에서 받아들이는 정보가 특벌히 사전 조건을 검증하지 않는다는 단점을 이용하는 공격 방식입니다.
     */

    /**
     * 스프링 시큐리티에서는 회원이나 계정에 대해서 User라는 용어를 사용합니다. User라는 단어를 사용할 때는 상당히 주의해야 합니다.
     * 이러한 이유 때문에 앞의 예제에서도 ClubMember와 같이 다른 이름을 사용하고 있습니다.
     *
     * 회원 아이디라는 용어 대신에 username이라는 단어를 사용합니다. 스프링 시큐리티에서는 username이라는 단어 자체가 회원을 구별할 수 있는
     * 식별 데이터를 의미합니다. 문자열로 처리하지 않는 점은 같지만 일반적으로 사용하는 회원의 이름이 아니라 오히려 id에 해당합니다.
     *
     * username과 password를 동시에 사용하지 않습니다. 스프링 시큐리티는 UserDetailService를 이용해서 회원의 존재만을 우선적으로 가져오고,
     * 이후에 password가 틀리면 'Bad Cridential(잘못된 자격증명)'이라는 결과를 만들어 냅니다.(인증)
     *
     * 사용자의 username과 password로 인증 과정이 긑나면 원하는 자원에(URL)에 접근할 수 있는 적절한 권한이 있는지를 확인하고
     * 인가 과정을 실행합니다. 이 과정에서는 'Access Denied'와 같은 결과가 만들어 집니다.
     *
     * 위와 같은 차이점을 처리하는 가장 핵심적인 부품은 UserDetailService입니다. UserDetailService는 아래와 같이 loadUserByUsername()이라는 단 하나의 메서드를 가지고 있습니다.
     * ---- UserDetails 인터페이스
     * loadUserByUsername() 말 그대로 username이라는 회원 아이디와 같은 식별 값으로 회원 정보를 가져옵니다. 메서드의 리턴 타입은 UserDetails라는 타입,
     *
     *
     */

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        // 오직 인증에 실패하는 경우에 처리를 전담
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    /**
     * ApiChekcFilter, 스프링의 빈으로 설정
     */
    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

}
