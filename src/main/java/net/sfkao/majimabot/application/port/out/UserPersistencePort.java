package net.sfkao.majimabot.application.port.out;

import net.sfkao.majimabot.domain.User;

import java.util.Optional;

public interface UserPersistencePort {
    Optional<User> findById(long id);
    void save(User user);
}