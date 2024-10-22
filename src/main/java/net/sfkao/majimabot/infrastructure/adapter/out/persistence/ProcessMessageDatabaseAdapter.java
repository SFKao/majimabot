package net.sfkao.majimabot.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.out.ProcessMessageDatabasePort;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.entity.WordEntity;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.mapper.WordMapper;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessMessageDatabaseAdapter implements ProcessMessageDatabasePort {
    private final WordRepository wordRepository;
    private final WordMapper wordMapper;

    @Override
    public List<Word> findByPalabraIn(List<String> palabras) {
        List<WordEntity> wordEntities = wordRepository.findByPalabraIn(palabras);
        return wordEntities.stream()
                .map(wordMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(List<Word> words) {
        List<WordEntity> wordEntities = words.stream()
                .map(wordMapper::toEntity)
                .collect(Collectors.toList());
        wordRepository.deleteAll(wordEntities);
    }
}