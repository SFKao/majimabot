package net.sfkao.majimabot.infrastructure.adapter.out.persistence.repository;

import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.WordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends MongoRepository<WordEntity, String> {
    List<WordEntity> getByPalabra(String palabra);

    List<WordEntity> findByPalabraIn(List<String> palabraList);

}
