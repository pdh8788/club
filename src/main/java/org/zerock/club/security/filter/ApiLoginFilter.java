package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * API를 이용한다면 일반적인 로그인의 URL이 아닌 별도의 URL로 로그인을 처리하는 것이 일반적입니다.
 * 폼 방식의 로그인과 달리 API는 URL이 변경되면 API를 이용하는 입장에서는 호출하는 URL을 변경해야만 하기 때문에 위험한 일입니다.
 *
 * ApiLoginFilter는 특정한 URL로 외부에서 로그인이 가능하도록 하고, 로그인이 성공하면 클라이언트가 Authorization 헤더의 값으로
 * 이용할 데이터를 전송할 것입니다.
 */
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("----------------ApiLoginFilter--------------------");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        if( email == null ){
            throw new BadCredentialsException("email cannot be null");
        }

        // email, pw를 파라미터로 받아서 실제 인증을 처리하는 것입니다.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pw);

        // TO-DO
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("---------------ApiLoginFilter-------------------");
        log.info("successfulAuthentication: " + authResult);
        log.info(authResult.getPrincipal());

        //email address
        String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;

        try {
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
