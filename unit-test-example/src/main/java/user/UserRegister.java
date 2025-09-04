package user;

public class UserRegister {

    private final WeakPasswordChecker weakPasswordChecker;
    private final UserRepository userRepository;
    private final EmailNotifier emailNotifier;


    public UserRegister(WeakPasswordChecker weakPasswordChecker, UserRepository userRepository,
        EmailNotifier emailNotifier) {
        this.weakPasswordChecker = weakPasswordChecker;
        this.userRepository = userRepository;
        this.emailNotifier = emailNotifier;
    }

    public void register(String id, String password, String email) {

        if (weakPasswordChecker.check(password)) {
            throw new WeakPasswordException();
        }
        validateDuplicateId(id);
        userRepository.save(new User(id, password, email));

        emailNotifier.send(email);
    }

    private void validateDuplicateId(String id) {
        userRepository.findById(id).ifPresent(user -> {
            throw new DupIdException();
        });
    }


}
