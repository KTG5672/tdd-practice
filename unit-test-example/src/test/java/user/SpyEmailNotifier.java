package user;

public class SpyEmailNotifier implements EmailNotifier {

    private boolean called;
    private String email;

    @Override
    public void send(String email) {
        called = true;
        this.email = email;
    }

    public boolean isCalled() {
        return called;
    }

    public String getEmail() {
        return email;
    }
}
