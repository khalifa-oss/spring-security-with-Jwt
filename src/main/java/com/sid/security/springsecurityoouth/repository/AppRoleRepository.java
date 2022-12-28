package com.sid.security.springsecurityoouth.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sid.security.springsecurityoouth.model.AppRole;
import com.sid.security.springsecurityoouth.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findAppRoleByRoleName(String role);

}
