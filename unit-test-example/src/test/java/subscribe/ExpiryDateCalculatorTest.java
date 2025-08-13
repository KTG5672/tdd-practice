package subscribe;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * 1. 서비스를 사용하려면 매달 1만 원을 선불로 납부한다. 납부일 기준으로 한 달 뒤가 서비스 만료일이 된다.
 * 2. 2개월 이상 요금을 납부할 수 있다.
 * 3. 10만 원을 납부하면 서비스를 1년 제공한다
 */
public class ExpiryDateCalculatorTest {

    @Test
    void 만원을_납부하면_한달_뒤가_만료일이_된다() {
        assertExpiryDate(LocalDate.of(2025, 8, 15), 10_000, LocalDate.of(2025, 9, 15));
        assertExpiryDate(LocalDate.of(2025, 8, 13), 10_000, LocalDate.of(2025, 9, 13));
    }

    void assertExpiryDate(LocalDate billingDate, int payAmount,
        LocalDate exceptExpiryDate) {
        ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();
        LocalDate actualExpiryDate = expiryDateCalculator.calculate(billingDate, payAmount);
        assertThat(actualExpiryDate).isEqualTo(exceptExpiryDate);
    }

}
