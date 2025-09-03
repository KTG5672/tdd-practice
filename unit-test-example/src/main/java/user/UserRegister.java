package user;

public class UserRegister {

    private final WeakPasswordChecker weakPasswordChecker;

    public UserRegister(WeakPasswordChecker weakPasswordChecker) {
        this.weakPasswordChecker = weakPasswordChecker;
    }

    public void register(String id, String password, String email) {
        if (weakPasswordChecker.check(password)) {
            throw new WeakPasswordException();
        }
    }


}
