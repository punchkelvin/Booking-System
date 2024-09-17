package com.example.bookingDemo.config;

import com.example.bookingDemo.security.JwtAuthenticationFilter;
import com.example.bookingDemo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//TODO Hybrid of Stateful and Stateless
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Bean
    //Security Filter chain is a bean that is responsible for configuring all the http security of our application
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        try {
            httpSecurity
                    .cors(Customizer.withDefaults())
                    .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()) // Disable CSRF
                    .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                            authorizationManagerRequestMatcherRegistry
                                    .requestMatchers("/token/**").permitAll()  // Permit / white list all requests to /token/**
                                    .requestMatchers("/auth/**").permitAll()
                                    .anyRequest().authenticated()  // Authenticate all other requests
                    )
//                    .authenticationProvider(authenticationProvider())
                    //In our jwt authentication filter, we implements once per request filter
                        // (means that every request should be authenticated)
                    //Hence the authentication state should not be stored in the security context holder
                    //Here, we make the session to be stateless, spring will create new session for each request
                    .sessionManagement(httpSecuritySessionManagementConfigurer ->
                            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session management

            // Make sure the JWT filter is not blocking this request, you can temporarily comment it out
             .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //storing password in plain text is insecure
        //this ensure that the password when submitted is hashed first, only then compared with the one hashed in the db
        return new BCryptPasswordEncoder(); //commonly used to check and hash password
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        //Dao is a common choice for spring authentication
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService); // custom user details service to retrieve user's info
//        authenticationProvider.setPasswordEncoder(passwordEncoder()); //used to verify user's password
//        return authenticationProvider;
//    }

}
