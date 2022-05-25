package com.config;

import com.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils
            .createAuthorityList("ROLE_ADMIN", "ROLE_USER");
    private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils
            .createAuthorityList("ROLE_USER");

    private User user;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(user.getRole() != null && user.getRole().equals("admin")){
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), ADMIN_ROLES);
        }
        else{
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), USER_ROLES);
        }
    }

    public void setUser(User user){
        this.user = user;
    }
}

