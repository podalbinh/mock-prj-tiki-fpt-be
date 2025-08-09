package com.vn.backend.controllers;

import com.vn.backend.dto.request.SignUpRequest;
import com.vn.backend.dto.response.ResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @PostMapping
    public ResponseData<Integer> registerUser(@RequestBody SignUpRequest signUpRequest) {
        var response = ResponseData.created(123123);
        return response;
    }
}
