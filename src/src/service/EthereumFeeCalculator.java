package service;

import model.enums.FeeLevel;

public class EthereumFeeCalculator implements FeeCalculator {
    private static final double GAS_PRICE_ECONOMICAL = 20.0;
    private static final double GAS_PRICE_STANDARD = 40.0;
    private static final double GAS_PRICE_FAST = 60.0;
    private static final int GAS_LIMIT = 21000;


    @Override
    public double calculateFees(FeeLevel feeLevel) {
        double gasPriceGwei;
        switch (feeLevel) {
            case ECONOMICAL:
                gasPriceGwei = GAS_PRICE_ECONOMICAL;
                break;
            case STANDARD:
                gasPriceGwei = GAS_PRICE_STANDARD;
                break;
            case FAST:
                gasPriceGwei = GAS_PRICE_FAST;
                break;
            default:
                throw new IllegalArgumentException("Unknown fee level: " + feeLevel);
        }
        return (gasPriceGwei * GAS_LIMIT) / 1_000_000_000.0 * 1500; // Fictional ETH price
    }
}
