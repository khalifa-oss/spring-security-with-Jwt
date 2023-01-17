package com.sid.security.springsecurityoouth.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.security.springsecurityoouth.filter.JwtAuthenticationFilter;
import com.sid.security.springsecurityoouth.model.AppRole;
import com.sid.security.springsecurityoouth.model.AppUser;
import com.sid.security.springsecurityoouth.model.LoginRequest;
import com.sid.security.springsecurityoouth.service.AppUserRoleServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
//@RequestMapping(value = "/",produces = MediaType.APPLICATION_XML_VALUE)
public class TestRestApi {
    private final AppUserRoleServiceImpl appUserRoleService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationManager authenticationManager;


    public TestRestApi(AppUserRoleServiceImpl appUserRoleService) {
        this.appUserRoleService = appUserRoleService;
    }

    @GetMapping (path = "/testData")
    public Map<String, Object> getDataTest() {
        return Map.of( "Message", "this is test" );
    }

    @GetMapping (path = "/getAppUsers")
    public List<AppUser> getAllAppUser() {
        return appUserRoleService.getListAppUser();


    }

    @GetMapping ("/getByUsername")
    public ResponseEntity<String> getAppUserByUsername(@RequestParam String username) {
        appUserRoleService.getAppUser( username );
        return new ResponseEntity( "founded.....", HttpStatus.OK );

    }

    //if the name of pathVariable was different to the path variable expose in the url so in the attribute name of annotation
    //we define the same name in the url
    @GetMapping ("/getAppRoleById/{id}")
    public AppRole getAppRoleById(@PathVariable (name = "id") Long id) {
        return appUserRoleService.getAppRoleById( id );

    }

    @PostMapping ("/addAppUser")
    public ResponseEntity<AppUser> addAppUser(@RequestBody AppUser appUser) {
        return new ResponseEntity( appUserRoleService.addAppUser( appUser ), HttpStatus.CREATED );

    }

    @PostMapping ("addAppRole")
    public AppRole addAppRole(@RequestBody AppRole appRole) {
        return appUserRoleService.addAppRole( appRole );
    }

    @PostMapping ("/addRoleToUser/{un}/{rl}")
    public void addRoleToUser(@PathVariable (name = "un") String username, @PathVariable (name = "rl") String role) {
        appUserRoleService.addRoleToUser( username, role );
    }

    @GetMapping ("/refreshToken")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = request.getHeader( "Authorization" );
        if (refreshToken != null && refreshToken.startsWith( "Bearer " )) {
            try {
                String jwtRefresh = refreshToken.substring( 7 );
                Algorithm algorithm = Algorithm.HMAC256( "mySecret123" );
                JWTVerifier jwtVerifier = JWT.require( algorithm ).build();
                DecodedJWT decodedJWT = jwtVerifier.verify( jwtRefresh );
                String username = decodedJWT.getSubject();
                AppUser user = appUserRoleService.getAppUser( username );
                String jwtAccessToken = JWT.create()
                        .withSubject( user.getUsername() )
                        .withExpiresAt( new Date( System.currentTimeMillis() + 1 * 60 * 1000 ) )
                        .withIssuer( request.getRequestURL().toString() )
                        .withClaim( "roles", user.getRoles().stream().map( r -> r.getRoleName() ).collect( Collectors.toList() ) )
                        .sign( algorithm );
                createToken( response, jwtRefresh, jwtAccessToken );


            } catch (Exception e) {
                response.setHeader( "Error-Message", e.getMessage() );


            }
        } else {
            throw new RuntimeException( "Refresh token required!!!!!! " );
        }
    }


    public static void createToken(HttpServletResponse response, String jwtRefresh, String jwtAccessToken) throws IOException {
        Map<String, String> idToken = new HashMap<>();
        idToken.put( "accessToken", jwtAccessToken );
        idToken.put( "refreshToken", jwtRefresh );
        response.setTrailerFields( () -> idToken );
        response.setContentType( "application/json" );
        new ObjectMapper().writeValue( response.getOutputStream(), idToken );
    }


}



