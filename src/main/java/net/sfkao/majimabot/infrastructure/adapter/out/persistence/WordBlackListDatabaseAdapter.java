package net.sfkao.majimabot.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.mapper.WordMapper;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.UserNotFoundException;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.UserEntity;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.WordEntity;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.repository.UserRepository;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.repository.WordRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class WordBlackListDatabaseAdapter implements WordBlackListDatabasePort {

    private final UserRepository userRepository; // Asumiendo que tienes un repositorio para la entidad `User`

    private final WordRepository wordRepository;

    private final WordMapper wordMapper = WordMapper.INSTANCE;

    @Override
    public void incrementMines(long userId, int count) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
        userEntity.setMinasExplotadas(userEntity.getMinasExplotadas() + count);
        userRepository.save(userEntity);
    }

    @Override
    public void incrementPoints(long userId, int points) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
        userEntity.setPuntos(userEntity.getPuntos() + points);
        userRepository.save(userEntity);
    }

    @Override
    public Mono<Word> save(Word word) {
        WordEntity wordEntity = wordMapper.toEntity(word);
        WordEntity savedEntity = wordRepository.save(wordEntity);
        return Mono.just(wordMapper.toDomain(savedEntity));
    }
}
