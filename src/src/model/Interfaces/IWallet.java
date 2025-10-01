package model.Interfaces;

import model.enums.CryptoCurrencyType;

import java.util.List;
import java.util.UUID;

public interface IWallet {
    UUID getId();

    String getAddress();

    double getBalance();

    CryptoCurrencyType getType();

    void deposit(double amount);

    void withdraw(double amount);

    void addTransaction(ITransaction transaction);

    List<ITransaction> getTransactions();
}
