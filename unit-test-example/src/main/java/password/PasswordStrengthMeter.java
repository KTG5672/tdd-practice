package password;

public class PasswordStrengthMeter {

    public PasswordLevel meter(String password) {
        if (password.length() < 8) {
            return PasswordLevel.NORMAL;
        }
        return PasswordLevel.STRONG;
    }
}
