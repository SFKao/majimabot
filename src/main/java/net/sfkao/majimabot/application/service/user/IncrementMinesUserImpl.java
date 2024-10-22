package net.sfkao.majimabot.application.service.user;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.domain.User;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.UserPersistenceAdapter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncrementMinesUserImpl implements IncrementMinesUser {

    private final UserPersistenceAdapter userPersistenceAdapter;

    @Override
    public void incrementMinas(long id, int cantidad) {
        Optional<User> usuarioOptional = userPersistenceAdapter.findById(id);
        User user = usuarioOptional.orElse(new User().setId(id));

        user.setMinasExplotadas(user.getMinasExplotadas() + cantidad);
        userPersistenceAdapter.save(user);

    }
}
