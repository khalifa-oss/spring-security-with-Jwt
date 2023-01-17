package com.sid.security.springsecurityoouth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
//    @Value(" ${publicKey}")
  public  String public_key="mySecret123";
//    @Value(" ${authHeader}")
    public  String authorization="Authorization";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals( "/refreshToken" )){
            filterChain.doFilter( request,response );
        }else {
       String authorizationToken = request.getHeader(  authorization );
       if(authorizationToken!=null && authorizationToken.startsWith("Bearer " )){
           try {

               String jwt = authorizationToken.substring ( 7 );
               Algorithm algorithm= Algorithm.HMAC256( public_key );
               JWTVerifier jwtVerifier= JWT.require( algorithm ).build();
               DecodedJWT decodedJWT= jwtVerifier.verify( jwt );
               String username = decodedJWT.getSubject();
               Collection<GrantedAuthority> authorities= Arrays.stream( decodedJWT.getClaim( "roles" ).asArray( String.class ) )
                       .map( r-> new SimpleGrantedAuthority( r ) ).collect( Collectors.toList());
               UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username,null,authorities  );
               SecurityContextHolder.getContext().setAuthentication( authenticationToken );
               filterChain.doFilter( request,response );


           }catch (Exception e){
               response.setHeader( "Error-Message",e.getMessage() );
               response.sendError( HttpServletResponse.SC_FORBIDDEN );
              // System.out.println(e.getStackTrace());

           }


       }else{
           //you can pass, but you can't get an authorization so the Security config (Filter chain you forbidden to access to any resource )
           filterChain.doFilter( request,response );
       }



    }
    }
}
