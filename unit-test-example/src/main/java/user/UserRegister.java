package user;

public class UserRegister {

    private final WeakPasswordChecker weakPasswordChecker;
    private final UserRepository userRepository;


    public UserRegister(WeakPasswordChecker weakPasswordChecker, UserRepository userRepository) {
        this.weakPasswordChecker = weakPasswordChecker;
        this.userRepository = userRepository;
    }

    public void register(String id, String password, String email) {
        if (weakPasswordChecker.check(password)) {
            throw new WeakPasswordException();
        }
        validateDuplicateId(id);
        userRepository.save(new User(id, password, email));
    }

    private void validateDuplicateId(String id) {
        userRepository.findById(id).ifPresent(user -> {
            throw new DupIdException();
        });
    }


}
