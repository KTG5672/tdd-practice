package password;

public class PasswordStrengthMeter {

    public PasswordLevel meter(String password) {
        if (password.length() < 8) {
            return PasswordLevel.NORMAL;
        }

        char[] charArray = password.toCharArray();
        boolean containNumber = false;
        for (char c : charArray) {
            if (c >= '0' && c <= '9') {
                containNumber = true;
                break;
            }
        }
        if (!containNumber) {
            return PasswordLevel.NORMAL;
        }
        return PasswordLevel.STRONG;
    }
}
