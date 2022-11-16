package br.com.narigaz.restwithspringbootandjava.controllers;

import br.com.narigaz.restwithspringbootandjava.data.vo.v1.security.AccountCredentialsVO;
import br.com.narigaz.restwithspringbootandjava.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint")
public class AuthController {
    @Autowired
    AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @PostMapping(value =  "/signin")
    @Operation(summary = "Authenticates a user and returns a token", description = "Authenticates a user and returns a token", tags = {"Authenticate"})
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid cliente Request");
        }
        var token = authServices.signin(data);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid cliente Request");
        }
        return token;
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value =  "/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (checkIfParamsIsNotNull(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid cliente Request");
        }
        var token = authServices.refreshToken(username, refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid cliente Request");
        }
        return token;
    }

    private static boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    private static boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }
}
