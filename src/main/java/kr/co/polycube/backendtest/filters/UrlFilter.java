package kr.co.polycube.backendtest.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class UrlFilter implements Filter {

    private static final Pattern CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9?&=:/]");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI() + "?" + request.getQueryString();

        if (CHAR_PATTERN.matcher(uri).find()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"timestamp\":\"" + LocalDateTime.now() + "\","
                            + "\"status\":400,"
                            + "\"reason\":\"유효한 URI가 아닙니다. 특수문자 포함 여부를 확인하세요.\","
                            + "\"path\":\"" + request.getRequestURI() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
