package user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 회원가입 서비스 테스트
 * - 암호가 약하면 가입이 불가
 */
public class UserRegisterTest {

    UserRegister userRegister;


    @Test
    @DisplayName("약한 암호면 가입 실패")
    void weakPassword() {
        assertThatThrownBy(() -> userRegister.register("id", "pass", "email"))
            .isInstanceOf(WeakPasswordException.class);
    }

}
