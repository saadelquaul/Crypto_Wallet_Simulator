package model;

import model.enums.CryptoType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wallet implements IWallet {
    private UUID id;
    private String address;
    private double balance;
    private CryptoType type;
    private List<ITransaction> transactions;


    public Wallet (CryptoType type){
        this.id = UUID.randomUUID();
        this.address = generateWalletAddressByType(type);
        this.balance = 0.0;
        this.type = type;
        this.transactions = new ArrayList<>();
    }

    @Override
    public UUID getId(){
        return this.id;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public CryptoType getType() {
        return this.type;
    }

    @Override
    public void deposit(double amount) {
       if(amount > 0) this.balance += amount;
    }

    @Override
    public void withdraw(double amount) {
    if( amount > 0 && this.balance >= amount) this.balance -= amount;
    }

    @Override
    public void addTransaction(ITransaction transaction) {
        this.transactions.add(transaction);
    }

    public List<ITransaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString(){
        return "Wallet { id = " + this.id +
                ", type=\'" + this.type + "\'" +
                ", address=\'" + this.address + "\'" +
                ", balance=" + String.format("%.2f", this.balance) +
                '}';

    }}
