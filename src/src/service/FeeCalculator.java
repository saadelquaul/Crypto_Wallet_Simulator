package service;

import model.enums.FeeLevel;

public interface FeeCalculator {
    double calculateFees(FeeLevel feeLevel);
}
