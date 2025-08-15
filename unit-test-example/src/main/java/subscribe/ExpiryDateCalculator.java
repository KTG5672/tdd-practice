package subscribe;

import static java.util.Objects.nonNull;

import java.time.LocalDate;

public class ExpiryDateCalculator {

    public LocalDate calculate(PayData payData) {
        if (nonNull(payData.getFirstBillingDate())) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(1);
            if (candidateExp.getDayOfMonth() != payData.getFirstBillingDate().getDayOfMonth()) {
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(1);
    }

}
