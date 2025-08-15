package subscribe;

import static java.util.Objects.nonNull;

import java.time.LocalDate;

public class ExpiryDateCalculator {

    public LocalDate calculate(PayData payData) {
        LocalDate billingDate = payData.getBillingDate();
        LocalDate firstBillingDate = payData.getFirstBillingDate();
        int monthsToAdd = 1;
        if (nonNull(firstBillingDate)) {
            LocalDate candidateExp = billingDate.plusMonths(monthsToAdd);
            if (candidateExp.getDayOfMonth() != firstBillingDate.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(firstBillingDate.getDayOfMonth());
            }
        }
        return billingDate.plusMonths(monthsToAdd);
    }

}
