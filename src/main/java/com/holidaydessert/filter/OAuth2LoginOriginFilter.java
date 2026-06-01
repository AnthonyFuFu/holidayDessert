package com.holidaydessert.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@Order(-200) // 必須小於 Spring Security 的 -100，才能搶先執行
public class OAuth2LoginOriginFilter extends OncePerRequestFilter {

    public static final String COOKIE_KEY_ORIGIN_PORT = "OAUTH2_ORIGIN_PORT";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 用 getServletPath() 而非 getRequestURI()，避免 context path 干擾
        String servletPath = request.getServletPath();

        if ("/front/google/login".equals(servletPath)) {
            int originPort = resolveOriginPort(request);
            System.out.println("[OAuth2LoginOriginFilter] 儲存 port: " + originPort);

            Cookie portCookie = new Cookie(COOKIE_KEY_ORIGIN_PORT, String.valueOf(originPort));
            portCookie.setPath("/");
            portCookie.setMaxAge(300);
            portCookie.setHttpOnly(true);
            response.addCookie(portCookie);
        }
        // 一定要呼叫，讓 Spring Security 繼續處理（重定向到 Google）
        filterChain.doFilter(request, response);
    }

    private int resolveOriginPort(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            int port = extractPortFromUrl(referer);
            if (port != -1) return port;
        }
        String origin = request.getHeader("Origin");
        if (origin != null) {
            int port = extractPortFromUrl(origin);
            if (port != -1) return port;
        }
        return request.getLocalPort();
    }

    private int extractPortFromUrl(String url) {
        try {
            int schemeEnd = url.indexOf("://");
            if (schemeEnd == -1) return -1;
            String afterScheme = url.substring(schemeEnd + 3);
            int slashIdx = afterScheme.indexOf('/');
            String hostPort = slashIdx != -1 ? afterScheme.substring(0, slashIdx) : afterScheme;
            int colonIdx = hostPort.lastIndexOf(':');
            if (colonIdx != -1) {
                return Integer.parseInt(hostPort.substring(colonIdx + 1));
            }
        } catch (NumberFormatException ignored) {}
        return -1;
    }
}

