package kr.co.polycube.backendtest.aops;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class LoggingAspect {

    private final HttpServletRequest request;

    @Pointcut("execution(* kr.co.polycube.backendtest.users..*(..))")
    public void userApiMethods() {}

    @Before("userApiMethods()")
    public void logClientAgent() {
        String clientAgent = request.getHeader("User-Agent");
        System.out.println("Client Agent: " + clientAgent);
    }

}
