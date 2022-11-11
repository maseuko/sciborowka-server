package pl.scb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.scb.models.ResponseMessage;
import pl.scb.models.AdminModel;
import pl.scb.repo.AdminRepo;
import pl.scb.services.SciborowkaAdminService;
import pl.scb.utils.JwtUtil;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/sciborowka-auth")
public class SciborowkaAuth {
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SciborowkaAdminService sciborowkaAdminService;

    @PostMapping("sign-up")
    private ResponseEntity<Object> signUp(@RequestParam("login") String login, @RequestParam("password") String password){
        Optional<AdminModel> admin = adminRepo.findByLogin(login);
        if(admin.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Invalid credentials.");
        }
        if(!new BCryptPasswordEncoder().matches(password,admin.get().getPassword())) {
            return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Invalid credentials.");
        }

        return new ResponseMessage(HttpStatus.OK).sendMessage("Signed in.", jwtUtil.generateTokens(admin.get(), true));
    }

    @PostMapping("refresh-tokens")
    private ResponseEntity<Object> refreshTokens(@RequestHeader("auth") String authToken, @RequestHeader("refresh") String refreshToken){
        try {
            return new ResponseMessage(HttpStatus.OK).sendMessage("Tokens refreshed.",this.jwtUtil.regenerateTokens(authToken, refreshToken));
        } catch (IOException e) {
            return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Invalid tokens.");
        }
    }

    @PostMapping("validate-tokens")
    private ResponseEntity<Object> checkForValidity(@RequestHeader("auth") String authToken, @RequestHeader("refresh") String refreshToken){
        if(this.jwtUtil.checkForTokenValidity(authToken) && this.jwtUtil.checkForTokenValidity(refreshToken)){
            return new ResponseMessage(HttpStatus.OK).sendMessage("Tokens are valid.");
        }
        return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Invalid tokens.");
    }
}