package util;

import model.enums.CryptoCurrencyType;

public class AddressValidator {

    public static boolean isValidAddress(String address, CryptoCurrencyType cryptoCurrencyType) {
        if( address == null || address.trim().isEmpty()) {
            return false;
        }
        switch(cryptoCurrencyType) {
            case BITCOIN: address.matches("^(1|3|bc1)[a-zA-HJ-NP-Z0-9]{25,34}$");
            case ETHEREUM: return address.matches("^0x[a-fA-F0-9]{40}$");
            default: return false;
        }
    }
}
