package com.example.demo.Controller;


import com.example.demo.Service.AuthService;
import com.example.demo.model.request.AuthenticationResponse;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.model.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Login with email and password ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "OK,token generated it last for an hour ")
            ,@ApiResponse(responseCode = "404",description = "Not found , no user with email entered")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest )  {
        System.out.println("login controller");
        System.out.println("Card Number: " + loginRequest.getEmail());
        System.out.println("login controller");
       return ResponseEntity.ok(authService.login(loginRequest));
    }


    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}