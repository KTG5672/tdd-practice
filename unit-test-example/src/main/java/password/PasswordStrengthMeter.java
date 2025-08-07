package password;

public class PasswordStrengthMeter {

    public PasswordLevel meter(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordLevel.INVALID;
        }
        int meterCount = 0;
        if (hasEnoughLength(password)) {
            meterCount++;
        }
        if (hasNumber(password)) {
            meterCount++;
        }
        if (hasUppercase(password)) {
            meterCount++;
        }
        return calculateStrength(meterCount);
    }

    private PasswordLevel calculateStrength(int meterCount) {
        return switch (meterCount) {
            case 0, 1 -> PasswordLevel.WEAK;
            case 2 -> PasswordLevel.NORMAL;
            default -> PasswordLevel.STRONG;
        };
    }

    private boolean hasEnoughLength(String password) {
        return password.length() >= 8;
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
