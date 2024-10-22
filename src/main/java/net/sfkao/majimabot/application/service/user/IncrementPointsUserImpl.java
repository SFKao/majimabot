package net.sfkao.majimabot.application.service.user;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.domain.User;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.UserPersistenceAdapter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncrementPointsUserImpl implements IncrementPointsUser {

    private final UserPersistenceAdapter userPersistenceAdapter;

    @Override
    public void incrementPuntos(long id, int cantidad) {
        Optional<User> usuarioOptional = userPersistenceAdapter.findById(id);
        User user = usuarioOptional.orElse(new User().setId(id));

        user.setPuntos(user.getPuntos() + cantidad);
        userPersistenceAdapter.save(user);

    }
}
