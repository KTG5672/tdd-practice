package user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UserRegisterMockTest {

    WeakPasswordChecker weakPasswordChecker = Mockito.mock(WeakPasswordChecker.class);

    UserRepository userRepository = Mockito.mock(UserRepository.class);

    EmailNotifier emailNotifier = Mockito.mock(EmailNotifier.class);

    UserRegister userRegister;

    @BeforeEach
    void setUp() {
        userRegister = new UserRegister(weakPasswordChecker, userRepository, emailNotifier);
    }

    @Test
    @DisplayName("약한 암호면 가입 실패")
    void weakPassword() {
        given(weakPasswordChecker.check("pass")).willReturn(true);
        assertThatThrownBy(() -> userRegister.register("id", "pass", "email"))
            .isInstanceOf(WeakPasswordException.class);
    }

    @Test
    @DisplayName("회원가입 시 암호 확인")
    void checkPassword() {
        userRegister.register("id", "pass", "email");
        then(weakPasswordChecker).should().check("pass");
    }

    @Test
    @DisplayName("이미 같은 ID가 존재하면 가입 실패")
    void dupIdExists() {
        given(userRepository.findById("id")).willReturn(Optional.of(new User("id", "pass", "email")));
        assertThatThrownBy(() -> userRegister.register("id", "pass", "email"))
            .isInstanceOf(DupIdException.class);
    }

    @Test
    @DisplayName("회원가입 완료 시 DB 저장")
    void registerAfterSaveDB() {
        userRegister.register("id", "pass", "email");
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 완료 후 메일 발송")
    void whenResisterThenSendMail() {
        userRegister.register("id", "pass", "email");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        then(emailNotifier).should().send(captor.capture());

        String realEmail = captor.getValue();
        assertThat(realEmail).isEqualTo("email");
    }

}
