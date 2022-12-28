package com.sid.security.springsecurityoouth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
//    @JsonIgnore it is th same
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private  String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roles;

}
