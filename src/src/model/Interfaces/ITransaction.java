package model.Interfaces;

import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import model.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ITransaction {
    UUID getId();
    String getSourceAddress();
    String getDestinationAddress();
    double getAmount();
    LocalDateTime getCreationTime();
    double getFees();
    FeeLevel getFeeLevel();
    TransactionStatus getStatus();
    CryptoCurrencyType getCryptoCurrencyType();


    void setStatus(TransactionStatus status);
    void setFees(double fees);



}
