package password;

public class PasswordStrengthMeter {

    public PasswordLevel meter(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordLevel.INVALID;
        }
        if (password.length() < 8) {
            return PasswordLevel.NORMAL;
        }

        boolean containNumber = hasNumber(password);
        if (!containNumber) {
            return PasswordLevel.NORMAL;
        }
        return PasswordLevel.STRONG;
    }

    private boolean hasNumber(String password) {
        char[] charArray = password.toCharArray();
        for (char c : charArray) {
            if (c >= '0' && c <= '9') {
                return true;
            }
        }
        return false;
    }
}
