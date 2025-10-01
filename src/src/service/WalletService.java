package service;


import model.Interfaces.IWallet;
import model.Wallet;
import model.enums.CryptoCurrencyType;
import repository.WalletRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class WalletService {
    private static final Logger LOGGER = Logger.getLogger(WalletService.class.getName());
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public IWallet creatWallet(CryptoCurrencyType cryptoCurrencyType) {
        IWallet Wallet;

        if (cryptoCurrencyType == CryptoCurrencyType.BITCOIN) {
            Wallet = new Wallet(cryptoCurrencyType);
        } else if (cryptoCurrencyType == CryptoCurrencyType.ETHEREUM) {
            Wallet = new Wallet(cryptoCurrencyType);
        } else {
            throw new IllegalArgumentException("Unsupported cryptocurrency : " + cryptoCurrencyType);
        }
        walletRepository.save(Wallet);
        LOGGER.info("Wallet created successfully : " +
                Wallet.getId() + " with address: " + Wallet.getAddress());
        return Wallet;
    }


    public Optional<IWallet> getWalletByAddress(String address) {
        return walletRepository.findByAddress(address);
    }

    public List<IWallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public void updateWallet(IWallet wallet) {
        walletRepository.save(wallet);
    }



}
