package com.vn.backend.dto.request;

import java.util.Date;
import java.util.Set;
public class SignUpRequest {
    private String password;
    private String email;
    private String confirmPassword;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
