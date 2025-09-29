package model;

import model.Interfaces.ITransaction;
import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import model.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements ITransaction {

    private UUID id;
    private String sourceAddress;
    private String destinationAddress;
    private double amount;
    private LocalDateTime creationDate;
    private double fees;
    private FeeLevel feeLevel;
    private TransactionStatus status;
    private CryptoCurrencyType cryptoCurrencyType;


    public Transaction (String sourceAddress, String destinationAddress, double amount,
                        FeeLevel feeLevel, CryptoCurrencyType cryptoCurrencyType) {
        this.id = UUID.randomUUID();
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.feeLevel = feeLevel;
        this.cryptoCurrencyType = cryptoCurrencyType;
        this.status = TransactionStatus.PENDING;

    }

    public Transaction (UUID id, String sourceAddress, String destinationAddress, double amount,
                        FeeLevel feeLevel, CryptoCurrencyType cryptoCurrencyType, TransactionStatus status,LocalDateTime creationDate,double fees) {
        this.id = id;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.feeLevel = feeLevel;
        this.cryptoCurrencyType = cryptoCurrencyType;
        this.status = status;
        this.creationDate = creationDate;
        this.fees = fees;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getSourceAddress() {
        return this.sourceAddress;
    }

    @Override
    public String getDestinationAddress() {
        return this.destinationAddress;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }

    @Override
    public LocalDateTime getCreationTime() {
        return this.creationDate;
    }

    @Override
    public double getFees() {
        return this.fees;
    }

    @Override
    public FeeLevel getFeeLevel() {
        return this.feeLevel;
    }

    @Override
    public TransactionStatus getStatus() {
        return this.status;
    }

    @Override
    public CryptoCurrencyType getCryptoCurrencyType() {
        return this.cryptoCurrencyType;
    }

    @Override
    public void setStatus(TransactionStatus status) {
            this.status = status;
    }

    @Override
    public void setFees(double fees) {
            this.fees = fees;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + this.id +
                ", sourceAddress=\'" + this.sourceAddress + '\'' +
        ", destinationAddress=\'" + this.destinationAddress + '\'' +
        ", amount=" + this.amount +
                ", creationDate=" + this.creationDate +
                ", fees=" + String.format("%.2f", fees) +
                ", feeLevel=" + this.feeLevel +
                ", status=" + this.status +
                ", cryptoCurrencyType=" + this.cryptoCurrencyType +
                "\'}";
        }
}
