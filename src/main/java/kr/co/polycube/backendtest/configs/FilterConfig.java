package kr.co.polycube.backendtest.configs;

import kr.co.polycube.backendtest.filters.UrlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UrlFilter> urlFilter() {
        FilterRegistrationBean<UrlFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new UrlFilter());
        filterRegistrationBean.addUrlPatterns("/users/*");

        return filterRegistrationBean;
    }

}
