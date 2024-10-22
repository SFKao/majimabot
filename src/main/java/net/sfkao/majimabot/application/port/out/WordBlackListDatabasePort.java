package net.sfkao.majimabot.application.port.out;

import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

public interface WordBlackListDatabasePort {
    void incrementMines(long userId, int count) throws UserNotFoundException;
    void incrementPoints(long userId, int points) throws UserNotFoundException;
    Mono<Word> save(Word word);
}