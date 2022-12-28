package com.sid.security.springsecurityoouth.config;

import com.sid.security.springsecurityoouth.filter.JwtAuthenticationFilter;
import com.sid.security.springsecurityoouth.filter.JwtAuthorizationFilter;
import com.sid.security.springsecurityoouth.service.UserDetailServiceImplement;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {


    private UserDetailServiceImplement userDetailServiceImplement;




    //    @Bean
//     public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//         return  new InMemoryUserDetailsManager(
//                 User.withUsername( "user1" ).password( "{noop}1234" ).authorities( "User" ).build(),
//                 User.withUsername( "user2" ).password( "{noop}1234" ).authorities( "User" ).build(),
//                 User.withUsername( "admin" ).password( "{noop}1234" ).authorities( "User,Admin" ).build()
//         );
//
//
//
//     }
//
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService( userDetailServiceImplement );
        authenticationProvider.setPasswordEncoder( getPasswordEncode() );
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return httpSecurity
                .csrf().disable()
                .addFilter(new JwtAuthenticationFilter( authenticationManager( authenticationConfiguration ))  )
                .addFilterBefore( new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class )
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
 //               .formLogin(formLogin->formLogin.loginPage(  ))

                .headers().frameOptions().disable().and()
                .authorizeRequests().antMatchers( "/h2-console/**","/refreshToken/**" ).permitAll().and()
                .authorizeRequests().antMatchers( HttpMethod.POST,"/addAppUser/**","/addAppRole/**" ).hasAuthority( "ADMIN" ).and()
                .authorizeRequests().antMatchers( HttpMethod.GET,"/getAppRoleById/**","/getByUsername/**","/getAppUsers/**").hasAuthority( "USER" ).and()
                .authorizeRequests().anyRequest().authenticated()
                .and()

                .build();


//                .httpBasic( Customizer.withDefaults())
//                .build();


    }

    @Bean
    public PasswordEncoder getPasswordEncode() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Lazy
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
