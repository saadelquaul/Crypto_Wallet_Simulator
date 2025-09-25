package model;

import model.enums.CryptoType;

import java.util.UUID;

public interface IWallet {
    UUID getId();
    String getAddress();
    double getBalance();
    CryptoType getType();
    void deposit(double amount);
    void withdraw(double amount);
    void addTransaction(ITransaction transaction);
}
