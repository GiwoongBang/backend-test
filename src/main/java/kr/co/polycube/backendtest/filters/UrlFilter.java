package kr.co.polycube.backendtest.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UrlFilter implements Filter {

    private static final Pattern CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9?&=:/]");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("timestamp", LocalDateTime.now().toString());
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            errorDetails.put("reason", "유효한 URI가 아닙니다. 특수문자 포함 여부를 확인하세요.");
            errorDetails.put("path", request.getRequestURI());

            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(errorDetails);

            response.getWriter().write(jsonResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
