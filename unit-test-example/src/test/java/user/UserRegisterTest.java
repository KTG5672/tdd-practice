package user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 회원가입 서비스 테스트
 * - 암호가 약하면 가입이 불가
 * - 동일 ID 가 있을 시 가입이 불가
 * - 가입 완료 후 메일 발송
 */
public class UserRegisterTest {

    UserRegister userRegister;
    StubWeakPasswordChecker weakPasswordChecker;
    FakeUserRepository fakeUserRepository;

    @BeforeEach
    void setUp() {
        weakPasswordChecker = new StubWeakPasswordChecker();
        fakeUserRepository = new FakeUserRepository();
        userRegister = new UserRegister(weakPasswordChecker, fakeUserRepository);
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
        fakeUserRepository.save(new User("id", "pass", "email"));

        assertThatThrownBy(() -> userRegister.register("id", "pass", "email"))
            .isInstanceOf(DupIdException.class);
    }

    @Test
    @DisplayName("회원가입 완료 시 DB 저장")
    void registerAfterSaveDB() {

        userRegister.register("id", "pass", "email");

        User findUser = fakeUserRepository.findById("id").orElseThrow(RuntimeException::new);
        assertThat(findUser.getId()).isEqualTo("id");
        assertThat(findUser.getPassword()).isEqualTo("pass");
        assertThat(findUser.getEmail()).isEqualTo("email");
    }

}
