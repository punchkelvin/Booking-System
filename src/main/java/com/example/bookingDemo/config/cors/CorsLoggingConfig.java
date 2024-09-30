package com.example.bookingDemo.config.cors;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Slf4j
//Filer allows fine grain control over what is logged
public class CorsLoggingConfig implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //Log relevant CORS information from the request
        log.info("CORS Request - Origin: {}, Method: {}, Headers: {}",
                httpRequest.getHeader("Origin"),
                httpRequest.getMethod(),
                httpRequest.getHeader("Access-Control-Request-Headers"));

        //Log relevant CORS information from the response (this logs after CORS is applied)
        chain.doFilter(request, response);

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("CORS Response - Access-Control-Allow-Origin: {}, " +
                "Access-Control-Allow-Methods: {}",
                httpResponse.getHeader("Access-Control-Allow-Origin"),
                httpResponse.getHeader("Access-Control-Allow-Methods"));

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
