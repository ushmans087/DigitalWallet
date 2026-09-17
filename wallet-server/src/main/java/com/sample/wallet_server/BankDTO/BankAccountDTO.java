package com.sample.wallet_server.BankDTO;


public class BankAccountDTO {

    private Long id;
    private String bankName;
    private String accountNumber;
    private String branch;
    private String ifscCode;
    private String accountType;
    private Double balance;
    private boolean verified;

    public BankAccountDTO() {
    }

    public BankAccountDTO(Long id, String bankName, String accountNumber,
                          String branch, String ifscCode,
                          String accountType, Double balance,
                          boolean verified) {
        this.id = id;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.ifscCode = ifscCode;
        this.accountType = accountType;
        this.balance = balance;
        this.verified = verified;
    }

    public Long getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
