package pl.scb.models;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
public class JwtTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String authToken;
    @Column
    private String refreshToken;
    @Column
    private long userId;

    public JwtTokens(String authToken, String refreshToken, long userId){
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
    public JwtTokens(long id, String authToken, String refreshToken, long userId){
        this.id = id;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
    public JwtTokens(){}
    public long getId() {
        return id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
