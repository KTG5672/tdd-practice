package user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String id);

    void save(User user);
}
