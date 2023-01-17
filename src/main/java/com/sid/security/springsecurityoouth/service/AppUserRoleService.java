package com.sid.security.springsecurityoouth.service;

import com.sid.security.springsecurityoouth.model.AppRole;
import com.sid.security.springsecurityoouth.model.AppUser;

import java.util.List;

public interface AppUserRoleService {
    void addRoleToUser(String username,String role);
    AppUser addAppUser(AppUser appUser);
    AppRole addAppRole(AppRole appRole );
    List<AppRole> addListAppRole(List<AppRole> list);
    List<AppUser> addListAppUser(List<AppUser> list);
    List<AppUser> getListAppUser();
    AppUser getAppUser(String username);
    AppRole getAppRoleById( Long id);
    boolean findUser(String username);



}
