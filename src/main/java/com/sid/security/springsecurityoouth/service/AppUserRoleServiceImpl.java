package com.sid.security.springsecurityoouth.service;

import com.sid.security.springsecurityoouth.model.AppRole;
import com.sid.security.springsecurityoouth.model.AppUser;
import com.sid.security.springsecurityoouth.repository.AppRoleRepository;
import com.sid.security.springsecurityoouth.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class AppUserRoleServiceImpl implements AppUserRoleService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    public AppUserRoleServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser= appUserRepository.findAppUserByUsername( username );
        AppRole appRole= appRoleRepository.findAppRoleByRoleName( role );
        appUser.getRoles().add( appRole );

    }

    @Override
    public AppUser addAppUser(AppUser appUser) {
        String pwd= appUser.getPassword();
        appUser.setPassword( passwordEncoder.encode( pwd ) );
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addAppRole(AppRole appRole) {


        return appRoleRepository.save( appRole );
    }


    @Override
    public List<AppRole> addListAppRole(List<AppRole> list) {
        return appRoleRepository.saveAll( list );
    }

    @Override
    public List<AppUser> addListAppUser(List<AppUser> list) {
        for(AppUser appUser:list){
            String pwd = appUser.getPassword();
            appUser.setPassword( passwordEncoder.encode(pwd) );
        }
        return appUserRepository.saveAll( list );
    }

    @Override
    public List<AppUser> getListAppUser() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser getAppUser(String username) {
        return appUserRepository.findAppUserByUsername( username );
    }

    @Override
    public AppRole getAppRoleById(Long id) {
        return appRoleRepository.findById( id ).get();
    }

    @Override
    public boolean findUser(String username) {
        return appUserRepository.existsAppUserByUsername( username );
    }
}
