package model;

import model.enums.CryptoType;
import model.enums.FeePriority;
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
    FeePriority getFeePriority();
    TransactionStatus getStatus();
    CryptoType getCryptoType();


    void setStatus(TransactionStatus status);
    void setFees(double fees);



}
