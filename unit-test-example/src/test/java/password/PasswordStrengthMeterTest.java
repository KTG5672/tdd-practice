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

    @Test
    void 조건_중_길이가_8글자_미만인_경우_강도는_보통() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordLevel level = meter.meter("Ab12345");
        assertThat(level).isEqualTo(PasswordLevel.NORMAL);
    }

    @Test
    void 조건_중_숫자를_포함하지않은_경우_강도는_보통() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordLevel level = meter.meter("Abcdefgh");
        assertThat(level).isEqualTo(PasswordLevel.NORMAL);
    }

}
