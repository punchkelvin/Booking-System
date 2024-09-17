package com.example.bookingDemo.security;

import com.example.bookingDemo.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
//Every time a request is received, it will trigger the filter
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired JwtTokenUtil jwtTokenUtil;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    //The request and response are our request and response respectively
        //This will allow us to intercept our request and extract data from it and add new data to the response
    //Filter chain is the chain of responsibility design pattern, contains list of other filters that we need to execute
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            username = jwtTokenUtil.extractUsername(jwt);
        }

        //Check if the username exist and if the user has already been authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            //Set Authentication if the token is valid
            if(jwtTokenUtil.validateToken(jwt, userDetails.getUsername())){
                //This token object is required by spring to update security context holder
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                //Adding additional details with our http request to the auth token
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Update the security context holder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //This will call the next filter within the chain
        filterChain.doFilter(request, response);
    }
}
