package user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 회원가입 서비스 테스트
 * - 암호가 약하면 가입이 불가
 * - 동일 ID 가 있을 시 가입이 불가
 */
public class UserRegisterTest {

    UserRegister userRegister;
    StubWeakPasswordChecker weakPasswordChecker;

    @BeforeEach
    void setUp() {
        weakPasswordChecker = new StubWeakPasswordChecker();
        userRegister = new UserRegister(weakPasswordChecker);
    }


    @Test
    @DisplayName("약한 암호면 가입 실패")
    void weakPassword() {
        weakPasswordChecker.setWeak(true);
        assertThatThrownBy(() -> userRegister.register("id", "pass", "email"))
            .isInstanceOf(WeakPasswordException.class);
    }

    @Test
    @DisplayName("이미 같은 ID가 존재하면 가입 실패")
    void dupIdExists() {
        assertThatThrownBy(() -> userRegister.register("id", "pass", "email"))
            .isInstanceOf(DupIdException.class);
    }

}
