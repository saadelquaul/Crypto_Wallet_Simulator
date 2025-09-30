package service;

import exception.InsufficientFundsException;
import exception.InvalidAddressException;
import model.Interfaces.ITransaction;
import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import repository.TransactionRepository;
import repository.WalletRepository;
import util.AddressValidator;

import java.util.logging.Logger;

public class TransactionService {
    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());
    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository)
    {
        this.transactionRepository  = transactionRepository;
    }

    public ITransaction createTransaction(String sourceAddress, String destinationAddress, double amount, FeeLevel feeLevel
    , CryptoCurrencyType cryptoCurrencyType) throws InvalidAddressException, InsufficientFundsException {
        if(!AddressValidator.isValidAddress(sourceAddress,cryptoCurrencyType)){
            throw new InvalidAddressException("Invalid source address for " + cryptoCurrencyType +
                    " : " + sourceAddress);
        }
        if(!AddressValidator.isValidAddress(destinationAddress,cryptoCurrencyType)) {
            throw new InvalidAddressException("Invalid destination address for " + cryptoCurrencyType + " : " +
                    destinationAddress);
        }

        if(amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }




    }

}
