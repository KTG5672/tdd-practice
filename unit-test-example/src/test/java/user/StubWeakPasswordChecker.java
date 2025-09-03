package user;

public class StubWeakPasswordChecker implements WeakPasswordChecker {

    private boolean weak;

    @Override
    public boolean check(String password) {
        return weak;
    }

    public void setWeak(boolean weak) {
        this.weak = weak;
    }
}
