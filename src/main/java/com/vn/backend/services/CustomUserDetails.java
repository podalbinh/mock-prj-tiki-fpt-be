package com.vn.backend.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.vn.backend.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String phone;
    private boolean isActive;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    public static CustomUserDetails mapUserToUserDetails(User user){
        List<GrantedAuthority> listAuthorities=user.getListRoles().stream()
                .map(roles->new SimpleGrantedAuthority(roles.getRolename().name()))
                .collect(Collectors.toList());
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getEmail(),
                user.getPhone(),
                user.getIsActive(),
                listAuthorities
        );
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}