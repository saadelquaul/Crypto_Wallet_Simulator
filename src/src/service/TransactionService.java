package service;

import model.Interfaces.ITransaction;
import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import repository.TransactionRepository;
import repository.WalletRepository;

import java.util.logging.Logger;

public class TransactionService {
    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());
    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository)
    {
        this.transactionRepository  = transactionRepository;
    }

    public ITransaction createTransaction(String sourceAddress, String destinationAddress, double amount, FeeLevel feeLevel
    , CryptoCurrencyType cryptoCurrencyType) throws InvalidAddressExeption, InsufficientFundsException {


    }

}
