package pl.scb.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.scb.models.JwtTokens;
import pl.scb.models.AdminModel;
import pl.scb.repo.JwtTokensRepo;
import pl.scb.repo.AdminRepo;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtUtil implements Serializable {
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private JwtTokensRepo jwtTokensRepo;
    @Autowired
    private AdminRepo adminRepo;

    private String generateAuthToken(AdminModel adminModel){
        return JWT
                .create()
                .withClaim("UID", adminModel.getId())
                .withClaim("Username", adminModel.getLogin())
                .withExpiresAt(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)))
                .withKeyId(this.secret)
                .sign(Algorithm.HMAC512(this.secret));
    }

    private String generateRefreshToken(AdminModel adminModel){
        return JWT
                .create()
                .withClaim("UID", adminModel.getId())
                .withExpiresAt(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                .withKeyId(this.secret)
                .sign(Algorithm.HMAC512(this.secret));
    }

    public Map<String, String> generateTokens(AdminModel sciborowkaAdminModel, boolean save){
        String jwtAuth = this.generateAuthToken(sciborowkaAdminModel);
        String jwtRefresh = this.generateRefreshToken(sciborowkaAdminModel);
        if(save){
            this.jwtTokensRepo.save(new JwtTokens(jwtAuth, jwtRefresh, sciborowkaAdminModel.getId()));
        }
        Map<String, String> tokens = new HashMap<>();
        tokens.put("authToken", jwtAuth);
        tokens.put("refreshToken", jwtRefresh);
        return tokens;
    }

    public long getUserIdFromToken(String token){
        DecodedJWT jwt = JWT.decode(token);
        final Base64.Decoder decoder = Base64.getUrlDecoder();
        String jsonPayload = new String(decoder.decode(jwt.getPayload()));
        JSONObject obj = new JSONObject(jsonPayload);
        return obj.getLong("UID");
    }

    public boolean checkForTokenValidity(String jwtToken){
        DecodedJWT jwt = JWT.decode(jwtToken);
        Algorithm alg = Algorithm.HMAC512(this.secret);
        try{
            alg.verify(jwt);
            return !jwt.getExpiresAt().before(Calendar.getInstance().getTime());
        }catch (SignatureVerificationException e){
            return false;
        }
    }

    public boolean checkForTokensKeyPairExistence(String oldAuthToken, String oldRefreshToken){
        Optional<JwtTokens> adminTokens = this.jwtTokensRepo.findByAuthTokenAndRefreshToken(oldAuthToken, oldRefreshToken);
        return adminTokens.isPresent();
    }

    public Map<String, String> regenerateTokens(String oldAuthToken, String oldRefreshToken) throws IOException {
        long UID =  this.getUserIdFromToken(oldRefreshToken);
        Optional<AdminModel> sciborowkaAdmin = this.adminRepo.findById(UID);
        Map<String, String> newTokens = null;
        Optional<JwtTokens> adminTokens = null;
        if(sciborowkaAdmin.isPresent()){
            newTokens = this.generateTokens(sciborowkaAdmin.get(), false);
            adminTokens = this.jwtTokensRepo.findByAuthTokenAndRefreshTokenAndUserId(oldAuthToken, oldRefreshToken, UID);
        }
        if(sciborowkaAdmin.isEmpty()){
            throw new IOException("Invalid credentials.");
        }
        if(adminTokens.isEmpty()){
            throw new IOException("There are no tokens for that user.");
        }
        JwtTokens actualTokens = adminTokens.get();
        actualTokens.setRefreshToken(this.generateRefreshToken(sciborowkaAdmin.get()));
        actualTokens.setAuthToken(this.generateAuthToken(sciborowkaAdmin.get()));
        this.jwtTokensRepo.save(actualTokens);
        newTokens.put("refresh", actualTokens.getRefreshToken());
        newTokens.put("auth", actualTokens.getAuthToken());
        return newTokens;
    }
}
