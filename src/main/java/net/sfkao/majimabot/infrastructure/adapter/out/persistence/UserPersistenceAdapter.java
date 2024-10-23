package net.sfkao.majimabot.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.out.UserPersistencePort;
import net.sfkao.majimabot.domain.User;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.UserEntity;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.mapper.UserMapper;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        userRepository.save(userEntity);
    }
}