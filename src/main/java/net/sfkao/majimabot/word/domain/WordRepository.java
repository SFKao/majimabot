package net.sfkao.majimabot.word.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends MongoRepository<Word, String> {
    List<Word> getByPalabra(String palabra);

    List<Word> findByPalabraIn(List<String> palabraList);

}
