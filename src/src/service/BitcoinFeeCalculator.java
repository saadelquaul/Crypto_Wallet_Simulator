package service;

import model.enums.FeeLevel;

public class BitcoinFeeCalculator implements FeeCalculator {
    private static final double SATOSHI_PER_BYTE_ECONOMIC = 50.0;
    private static final double SATOSHI_PER_BYTE_STANDARD = 100.0;
    private static final double SATOSHI_PER_BYTE_RAPID = 150.0;
    private static final int TRANSACTION_SIZE_BYTES = 226;

    @Override
    public double calculateFees(FeeLevel feeLevel) {
        double satoshiPerByte;
        switch (feeLevel) {
            case ECONOMICAL:
                satoshiPerByte = SATOSHI_PER_BYTE_ECONOMIC;
                break;
            case STANDARD:
                satoshiPerByte = SATOSHI_PER_BYTE_STANDARD;
                break;
            case FAST:
                satoshiPerByte = SATOSHI_PER_BYTE_RAPID;
                break;
            default:
                throw new IllegalArgumentException("Unknown fee level: " +
                        feeLevel);
        }

        return (satoshiPerByte * TRANSACTION_SIZE_BYTES) / 100_000_000.0 *
                20000;
    }
}
