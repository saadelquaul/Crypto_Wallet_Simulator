package model;

import model.enums.CryptoType;
import model.enums.FeePriority;
import model.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements ITransaction{

    private UUID id;
    private String sourceAddress;
    private String destinationAddress;
    private double amount;
    private LocalDateTime creationDate;
    private double fees;
    private FeePriority feePriority;
    private TransactionStatus status;
    private CryptoType cryptoCurrency;


    public Transaction (String sourceAddress, String destinationAddress, double amount,
                        FeePriority feePriority, CryptoType cryptoCurrency) {
        this.id = UUID.randomUUID();
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.feePriority = feePriority;
        this.cryptoCurrency = cryptoCurrency;
        this.status = TransactionStatus.PENDING;

    }

    public Transaction (UUID id, String sourceAddress, String destinationAddress, double amount,
                        FeePriority feePriority, CryptoType cryptoCurrency, TransactionStatus status) {
        this.id = id;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.feePriority = feePriority;
        this.cryptoCurrency = cryptoCurrency;
        this.status = status;
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
    public FeePriority getFeePriority() {
        return this.feePriority;
    }

    @Override
    public TransactionStatus getStatus() {
        return this.status;
    }

    @Override
    public CryptoType getCryptoCurrency() {
        return this.cryptoCurrency;
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
                ", feeLevel=" + this.feePriority +
                ", status=" + this.status +
                ", cryptoCurrencyType=" + this.cryptoCurrency +
                "\'}";
        }
}
