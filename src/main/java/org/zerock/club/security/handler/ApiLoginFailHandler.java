package org.zerock.club.security.handler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 인증 실패 처리
 * 1. APILoginFilter에서 인증에 실패하는 경우 API 서버는 일반 화면이 아니라 JSON 결과가 전송되도록 수정해야 하고
 *    성공하는 경우에는 클라이언트가 보관할 인증 토큰이 전송 되어야 합니다.
 *     AbstractAuthenticationProcessingFilter에는 setAuthenticationFailureHandler()를 이용해서 인증에 실패했을 경우 처리를 지정할 수 있습니다.
 *     security패키지의 handler 패키지 내 ApiLoginFailHandler 클래스를 추가합니다.
 *
 */
@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login fail handler...............");
        log.info(exception.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // json 리턴
        response.setContentType("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        String message = exception.getMessage();
        json.put("code", "401");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
