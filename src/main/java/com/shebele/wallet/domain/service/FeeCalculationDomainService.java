package com.shebele.wallet.domain.service;

import com.shebele.wallet.domain.valueobject.Money;
import org.springframework.stereotype.Component;

@Component
public class FeeCalculationDomainService {

    private static final double STANDARD_RATE = 0.01;   // 1%
    private static final double PREMIUM_RATE = 0.005;   // 0.5%
    private static final Money HIGH_AMOUNT_THRESHOLD = Money.of(10000);
    private static final Money MIN_FEE = Money.of(1);
    private static final Money MAX_FEE = Money.of(100);

    /**
     * Calculate transfer fee based on amount only
     * (In real system, you would also check user tier, first transfer of day, etc.)
     */
    public Money calculateTransferFee(Money amount) {
        // Calculate rate based on amount
        double rate = amount.isGreaterThan(HIGH_AMOUNT_THRESHOLD) ? PREMIUM_RATE : STANDARD_RATE;

        // Calculate fee
        Money calculatedFee = Money.of(amount.getAmount().doubleValue() * rate);

        // Apply min/max limits
        if (calculatedFee.isLessThan(MIN_FEE)) {
            return MIN_FEE;
        }
        if (calculatedFee.isGreaterThan(MAX_FEE)) {
            return MAX_FEE;
        }

        return calculatedFee;
    }

    /**
     * Calculate agent commission
     */
    public Money calculateAgentCommission(Money amount, double commissionRatePercent) {
        Money commission = Money.of(amount.getAmount().doubleValue() * (commissionRatePercent / 100));

        Money minCommission = Money.of(0.50);
        if (commission.isLessThan(minCommission)) {
            return minCommission;
        }

        return commission;
    }
}