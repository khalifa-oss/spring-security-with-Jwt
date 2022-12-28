package com.sid.security.springsecurityoouth.repository;

import com.sid.security.springsecurityoouth.model.AppRole;
import com.sid.security.springsecurityoouth.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String username);


}
