package password;

public class PasswordStrengthMeter {

    public PasswordLevel meter(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordLevel.INVALID;
        }
        boolean hasEnoughLength = password.length() >= 8;
        boolean containsNumber = hasNumber(password);
        boolean containsUppercase = hasUppercase(password);
        if (hasEnoughLength && !containsNumber && !containsUppercase) {
            return PasswordLevel.WEAK;
        }
        if (!hasEnoughLength) {
            return PasswordLevel.NORMAL;
        }
        if (!containsNumber) {
            return PasswordLevel.NORMAL;
        }
        if (!containsUppercase) {
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

    private boolean hasUppercase(String password) {
        char[] charArray = password.toCharArray();
        for (char c : charArray) {
            if (c >= 'A' && c <= 'Z') {
                return true;
            }
        }
        return false;
    }
}
