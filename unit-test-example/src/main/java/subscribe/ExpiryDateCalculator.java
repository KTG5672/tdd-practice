package subscribe;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

    private static final int YEARS_UNIT_AMOUNT = 100_000;
    private static final int MONTHS_UNIT_AMOUNT = 10_000;

    public LocalDate calculate(PayData payData) {
        LocalDate billingDate = payData.getBillingDate();
        LocalDate firstBillingDate = payData.getFirstBillingDate();
        int payAmount = payData.getPayAmount();

        int monthsToAdd = calculateMonthsToAdd(payAmount);

        LocalDate candidateExp = billingDate.plusMonths(monthsToAdd);
        if (nonNull(firstBillingDate)) {
            candidateExp = adjustByFirstBillingDate(candidateExp, firstBillingDate);
        }
        return candidateExp;
    }

    private int calculateMonthsToAdd(int payAmount) {
        int yearsToAdd = payAmount / YEARS_UNIT_AMOUNT;
        int monthsToAdd = (payAmount % YEARS_UNIT_AMOUNT) / MONTHS_UNIT_AMOUNT;
        return (yearsToAdd * 12) + monthsToAdd;
    }

    private LocalDate adjustByFirstBillingDate(LocalDate candidateExp, LocalDate firstBillingDate) {
        YearMonth candidateExpYearMonth = YearMonth.from(candidateExp);
        if (candidateExpYearMonth.lengthOfMonth() < firstBillingDate.getDayOfMonth()) {
            return candidateExp.withDayOfMonth(
                candidateExpYearMonth.lengthOfMonth()
            );
        }
        if (candidateExp.getDayOfMonth() != firstBillingDate.getDayOfMonth()) {
            return candidateExp.withDayOfMonth(firstBillingDate.getDayOfMonth());
        }
        return candidateExp;
    }

}
