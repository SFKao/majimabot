package net.sfkao.majimabot.application.port.out;

import net.sfkao.majimabot.domain.Word;

import java.util.List;

public interface ProcessMessageDatabasePort {
    List<Word> findByPalabraIn(List<String> palabras);
    void deleteAll(List<Word> wordEntities);
}
