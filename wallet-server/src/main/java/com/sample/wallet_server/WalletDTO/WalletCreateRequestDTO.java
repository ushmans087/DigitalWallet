package com.sample.wallet_server.WalletDTO;

/*
Request from AuthService to create a wallet for user when registering
{
    username;
    userId;
    email;
}
 */

public class WalletCreateRequestDTO {

    private Long userId;
    private String username;
    private String email;

    public WalletCreateRequestDTO() {
    }

    public WalletCreateRequestDTO(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}