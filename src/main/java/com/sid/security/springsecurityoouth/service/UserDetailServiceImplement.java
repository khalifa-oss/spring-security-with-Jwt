package com.sid.security.springsecurityoouth.service;

import com.sid.security.springsecurityoouth.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImplement implements UserDetailsService {
    @Lazy
    @Autowired
    private AppUserRoleServiceImpl appUserRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            AppUser appUser = appUserRoleService.getAppUser( username );
//            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            appUser.getRoles().forEach( r->authorities.add( new SimpleGrantedAuthority(r.getRoleName() ) ));

            return new User(appUser.getUsername(),appUser.getPassword() ,appUser.getRoles().stream()
                    .map( r-> new SimpleGrantedAuthority( r.getRoleName() ) ).collect( Collectors.toList()) );
        }

    }

