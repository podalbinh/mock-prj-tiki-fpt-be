package com.vn.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token; 
    private String type="Bearer ";
    private String email;
    private String phone;
    private List<String> listRoles;
    public JwtResponse(String token, String email, String phone, List<String> listRoles) {
        this.token = token;
        this.email = email;
        this.phone = phone;
        this.listRoles = listRoles;
    }

}
