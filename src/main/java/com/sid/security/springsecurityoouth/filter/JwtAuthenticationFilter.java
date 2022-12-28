package com.sid.security.springsecurityoouth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.security.springsecurityoouth.web.TestRestApi;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    System.out.println("Attempt authentication.......");

    String username= request.getParameter( "username" );
    String password = request.getParameter( "password" );
    System.out.println(username);
    System.out.println(password);
    UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken( username,password );
     return  authenticationManager.authenticate( authenticationToken );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successful authentication.......");

        User user= (User) authResult.getPrincipal();
        Algorithm algorithm= Algorithm.HMAC256( "mySecret123" );
        String jwtAccessToken= JWT.create()
                .withSubject( user.getUsername() )
                .withExpiresAt( new Date(System.currentTimeMillis()+1*60*1000) )
                .withIssuer( request.getRequestURL().toString() )
                .withClaim( "roles",user.getAuthorities().stream().map( ga->ga.getAuthority() ).collect( Collectors.toList()) )
                .sign( algorithm );
        String jwtRefreshToken= JWT.create()
                .withSubject( user.getUsername() )
                .withExpiresAt( new Date(System.currentTimeMillis()+10*60*1000) )
                .withIssuer( request.getRequestURL().toString() )
                .sign( algorithm );
        TestRestApi.createToken( response, jwtRefreshToken, jwtAccessToken );
//        response.setHeader( "Authorization",jwtAccessToken );
        System.out.println("********* "+response.getHeaderNames());

    }
}
