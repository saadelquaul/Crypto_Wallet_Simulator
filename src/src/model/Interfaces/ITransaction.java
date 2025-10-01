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

    void setFees(double fees);

    FeeLevel getFeeLevel();

    TransactionStatus getStatus();

    CryptoCurrencyType getCryptoCurrencyType();


}
