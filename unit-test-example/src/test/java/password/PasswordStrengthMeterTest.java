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
 *  1개 이하 - "약함"
 *  빈 값 - "유효하지 않음"
 */

public class PasswordStrengthMeterTest {

    @Test
    void 모든_조건을_충족하는_경우_강도는_강함() {
        PasswordLevel level = getPasswordLevel("A1bcdefg");
        assertThat(level).isEqualTo(PasswordLevel.STRONG);
    }

    @Test
    void 조건_중_길이가_8글자_미만인_경우_강도는_보통() {
        PasswordLevel level = getPasswordLevel("Ab12345");
        assertThat(level).isEqualTo(PasswordLevel.NORMAL);
    }

    @Test
    void 조건_중_숫자를_포함하지않은_경우_강도는_보통() {
        PasswordLevel level = getPasswordLevel("Abcdefgh");
        assertThat(level).isEqualTo(PasswordLevel.NORMAL);
    }

    private static PasswordLevel getPasswordLevel(String password) {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        return meter.meter(password);
    }

    @Test
    void 패스워드가_null_또는_빈값일_경우_유효하지_않음() {
        PasswordLevel resultFromNull = getPasswordLevel(null);
        assertThat(resultFromNull).isEqualTo(PasswordLevel.INVALID);
        PasswordLevel resultFromEmpty = getPasswordLevel("");
        assertThat(resultFromEmpty).isEqualTo(PasswordLevel.INVALID);
    }

    @Test
    void 조건_중_대문자를_포함하지않은_경우_강도는_보통() {
        PasswordLevel result = getPasswordLevel("ab12cdefgh");
        assertThat(result).isEqualTo(PasswordLevel.NORMAL);
    }

    @Test
    void 조건_중_길아거_8글자_이상인_조건만_충족한_경우_강도는_약함() {
        PasswordLevel result = getPasswordLevel("abcdefghig");
        assertThat(result).isEqualTo(PasswordLevel.WEAK);
    }


}
