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
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2025, 8, 15))
                .payAmount(10_000)
                .build(),
            LocalDate.of(2025, 9, 15));

        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2025, 8, 13))
                .payAmount(10_000)
                .build(),
            LocalDate.of(2025, 9, 13));
    }

    /**
     * 8/31 -> 9/30
     */
    @Test
    void 납부일과_한달_뒤가_일자가_같지_않다() {
        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2025, 8, 31))
                .payAmount(10_000)
                .build(),
            LocalDate.of(2025, 9, 30));

        assertExpiryDate(PayData.builder()
                .billingDate(LocalDate.of(2025, 1, 31))
                .payAmount(10_000)
                .build(),
            LocalDate.of(2025, 2, 28));
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_납부() {
        PayData payData = PayData.builder()
            .firstBillingDate(LocalDate.of(2025, 1, 31))
            .billingDate(LocalDate.of(2025, 2, 28))
            .payAmount(10_000)
            .build();
        assertExpiryDate(payData, LocalDate.of(2025, 3, 31));
    }

    @Test
    void 이만원_이상_납부하면_비례하여_만료일_계산() {
        assertExpiryDate(
            PayData.builder()
                .billingDate(LocalDate.of(2025, 8, 1))
                .payAmount(20_000)
                .build(),
            LocalDate.of(2025, 10, 1)
        );
        assertExpiryDate(
            PayData.builder()
                .billingDate(LocalDate.of(2025, 8, 1))
                .payAmount(30_000)
                .build(),
            LocalDate.of(2025, 11, 1)
        );
    }

    @Test
    void 첫_납부일과_만료일의_일자가_다를때_이만원_이상_납부() {
        assertExpiryDate(
            PayData.builder()
                .firstBillingDate(LocalDate.of(2025, 1, 31))
                .billingDate(LocalDate.of(2025, 2, 28))
                .payAmount(20_000)
                .build(),
            LocalDate.of(2025, 4, 30)
        );
    }

    void assertExpiryDate(PayData payData, LocalDate exceptExpiryDate) {
        ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();
        LocalDate actualExpiryDate = expiryDateCalculator.calculate(payData);
        assertThat(actualExpiryDate).isEqualTo(exceptExpiryDate);
    }

}
