package password;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * 암호 검사기
 * - 검사 규칙
 *  1. 길이가 8글자 이상
 *  2. 0부터 9사이의 숫자를 포함
 *  3. 대문자 포함
 * - 암호 강도
 *  3개 - "강함"
 *  2개 - "보통"
 *  1개 - "약함"
 */

public class PasswordStrengthMeterTest {

    @Test
    void 모든_조건을_충족하는_경우_강도는_강함() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordLevel level = meter.meter("A1bcdefg");
        assertThat(level).isEqualTo(PasswordLevel.STRONG);
    }

}
