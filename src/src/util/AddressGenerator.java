package util;

import model.enums.CryptoCurrencyType;

import java.util.UUID;

public class AddressGenerator {
    public static String generateWalletAddressByType(CryptoCurrencyType cryptoCurrencyType) {
        switch (cryptoCurrencyType) {
            case BITCOIN: {
                String prefix = "bc1";
                return prefix + UUID.randomUUID().toString().
                        replace("-", "").
                        substring(0, 30);
            }
            case ETHEREUM: {
                String prefix = "0x";
                return prefix + UUID.randomUUID().toString().
                        replace("-", "").
                        substring(0, 30);
            }
            default:
                throw new IllegalArgumentException("Unsupported CryptoCurrencyType: " + cryptoCurrencyType);
        }
    }
}
