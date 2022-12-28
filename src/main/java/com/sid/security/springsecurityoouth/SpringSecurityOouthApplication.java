package com.sid.security.springsecurityoouth;

import com.sid.security.springsecurityoouth.model.AppRole;
import com.sid.security.springsecurityoouth.model.AppUser;
import com.sid.security.springsecurityoouth.service.AppUserRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringSecurityOouthApplication {
//    @Autowired
//    AppUserRoleServiceImpl appUserRoleService;

    public static void main(String[] args) {
        SpringApplication.run( SpringSecurityOouthApplication.class, args );
    }

@Bean
    CommandLineRunner start( AppUserRoleServiceImpl appUserRoleService) {
        return (arg -> {
            List<AppRole> listRole = List.of( new AppRole( null, "USER" ),
                    new AppRole( null, "ADMIN" ),
                    new AppRole( null, "CUSTOMER_MANAGER" ),
                    new AppRole( null, "PRODUCT_MANAGER" ) );
            appUserRoleService.addListAppRole( listRole );
            //

            List<AppUser> listUser = List.of(
                    new AppUser( null, "user1", "1234", new ArrayList<AppRole>() ),
                    new AppUser( null, "admin1", "1234", new ArrayList<AppRole>() ),
                    new AppUser( null, "user2", "1234", new ArrayList<AppRole>() ),
                    new AppUser( null, "user3", "1234", new ArrayList<AppRole>() ) );

            appUserRoleService.addListAppUser( listUser);
            appUserRoleService.addRoleToUser( "user1","USER" );
            appUserRoleService.addRoleToUser( "user1","ADMIN" );
            appUserRoleService.addRoleToUser( "admin1","ADMIN" );
            appUserRoleService.addRoleToUser( "user2","CUSTOMER_MANAGER" );
            appUserRoleService.addRoleToUser( "user3","PRODUCT_MANAGER" );

            //

        });
    }

}
