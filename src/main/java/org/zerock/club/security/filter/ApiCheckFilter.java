package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.club.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * OncePerRequestFilter는 추상 클래스로 제공되는 필터로 가장 일반적이며, 매번 동작하는 기본적인 필터입니다.
 * OncePerRequestFilter는 추상 클래스이므로 이를 사용하기 위해서는 상속으로 구현해야 합니다.
 */

@Log4j2
public class  ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    /**
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     *
     * 특정한 API를 호출하는 클라이언트에서는 다른 서버나 Application으로 실행되기 때문에 쿠키나 세션을 활용할 수 없습니다.
     * 이러한 제약 때문에 API를 호출하는 경우에는 Request를 전송할때 Http헤더 메시지에 특별한 값을 지정해서 전송합니다.
     *
     * Authorization 헤더는 이러한 용도로 사용합니다. 클라이언트에서 전송한 Request에 포함된 Authorization 헤더의 값을
     * 파악해서 사용자가 정상적인 요청인지를 알아내는 것입니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTURI: " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        if(antPathMatcher.match(pattern, request.getRequestURI())){

            log.info("ApiCheckFilter....................................");
            log.info("ApiCheckFilter....................................");
            log.info("ApiCheckFilter....................................");

            boolean checkHeader = checkAuthHeader(request);

            if(checkHeader){
                filterChain.doFilter(request, response);
                return;
            } else{
                //헤더 검증 실패 처리
                // 1. AuthenticationManager
                // 2.JSON 포맷 에러 메시지 전송 
                // - 2번에 해당되는 내용
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴 및 한글깨짐 수정
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code","403");
                json.put("message",message);

                PrintWriter out =response.getWriter();
                out.print(json);
                 return;
            }
        }



        //filterChain.doFilter()는 다음 필터의 단계로 넘어가는 역할을 위해서 필요합니다.
        filterChain.doFilter(request, response);
    }

    /**
     *
     * Authorization 헤더 처리
     * 특정한 API를 호출하는 클라이언트에서는 다른 서버나 Application으로 실행되기 때문에 쿠키나 세션을 활용할 수 없습니다.
     * 이러한 제약 때문에 api를 호출하는 경우에는 Request를 전송할 때 Http 헤더 메시지에 특별한 값을 지정해서 전송합니다.
     *
     * Authrization헤더는 이러한 용도로 사용합니다. 클라이언트에서 전송한 Request에 포함된 Authorization 헤더의 값을 파악해서
     * 사용자가 정상적인 요청인지를 알아내는 것입니다.
     *
     */
    private boolean checkAuthHeader(HttpServletRequest request){

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist : " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + email);
                checkResult = email.length() > 0;
            } catch ( Exception e ){
                e.printStackTrace();
            }
        }

        return checkResult;
    }

}
