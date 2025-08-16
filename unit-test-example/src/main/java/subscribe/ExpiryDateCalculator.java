package subscribe;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

    public LocalDate calculate(PayData payData) {
        LocalDate billingDate = payData.getBillingDate();
        LocalDate firstBillingDate = payData.getFirstBillingDate();
        int monthsToAdd = payData.getPayAmount() / 10_000;
        if (nonNull(firstBillingDate)) {
            LocalDate candidateExp = billingDate.plusMonths(monthsToAdd);
            YearMonth candidateExpYearMonth = YearMonth.from(candidateExp);
            if (candidateExpYearMonth.lengthOfMonth() < firstBillingDate.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(
                    candidateExpYearMonth.lengthOfMonth()
                );
            }
            if (candidateExp.getDayOfMonth() != firstBillingDate.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(firstBillingDate.getDayOfMonth());
            }
        }
        return billingDate.plusMonths(monthsToAdd);
    }

}
