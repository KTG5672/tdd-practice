package calculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test
    void plus_two_numbers() {
        int result = Calculator.plus(1, 2);
        assertThat(result).isEqualTo(3);
    }

}
