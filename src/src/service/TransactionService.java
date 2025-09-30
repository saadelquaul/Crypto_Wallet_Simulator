package service;

import exception.InsufficientFundsException;
import exception.InvalidAddressException;
import model.Interfaces.ITransaction;
import model.Interfaces.IWallet;
import model.Transaction;
import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import model.enums.TransactionStatus;
import repository.TransactionRepository;
import util.AddressValidator;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class TransactionService {
    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository, WalletService walletService)
    {
        this.transactionRepository  = transactionRepository;
        this.walletService = walletService;
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

        Optional<IWallet> sourceWalletOpt = walletService.getWalletByAddress(sourceAddress);
        if(!sourceWalletOpt.isPresent()) {
            throw new InsufficientFundsException("Source wallet not found for address " + sourceAddress);
        }

        IWallet sourceWallet = sourceWalletOpt.get();

        FeeCalculator feeCalculator;
        if(cryptoCurrencyType == CryptoCurrencyType.BITCOIN) {
            feeCalculator = new BitcoinFeeCalculator();
        } else if (cryptoCurrencyType == CryptoCurrencyType.ETHEREUM) {
            feeCalculator = new EthereumFeeCalculator();
        } else {
            throw new IllegalArgumentException("Unsupported cryptocurrency type: "+ cryptoCurrencyType);
        }
        double fees = feeCalculator.calculateFees(feeLevel);
        LOGGER.info("Calculated fees for " + cryptoCurrencyType + " transaction with " + feeLevel +
                " level: " + fees);

        double totalAmount = amount + fees;
        if(sourceWallet.getBalance() < totalAmount) {
            throw new InsufficientFundsException("Insufficient Funds in wallet " + sourceAddress + ". Required: " +
                    totalAmount + ", Available: " + sourceWallet.getBalance());
        }

        Transaction transaction = new Transaction(sourceAddress,destinationAddress,amount,feeLevel, cryptoCurrencyType);
        transaction.setFees(fees);

        sourceWallet.withdraw(totalAmount);
        transactionRepository.save(transaction);
        sourceWallet.addTransaction(transaction);

        LOGGER.info("Transaction " + transaction.getId() + "Created");

        return transaction;
    }

    public Optional<ITransaction> getTransactionById(UUID id){
        return transactionRepository.findById(id);
    }

    public void updateTransactionStatus(UUID transactionId, TransactionStatus newStatus) {
        Optional<ITransaction> transactionOpt = transactionRepository.findById(transactionId);
        if(transactionOpt.isPresent()) {
            ITransaction transaction = transactionOpt.get();
            transaction.setStatus(newStatus);
            transactionRepository.save(transaction);
            LOGGER.info("Transaction " + transactionId + " status updated to " + newStatus);
        }
    }

}
