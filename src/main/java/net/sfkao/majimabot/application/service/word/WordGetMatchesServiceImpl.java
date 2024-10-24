package net.sfkao.majimabot.application.service.word;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.out.ProcessMessageDatabasePort;
import net.sfkao.majimabot.application.util.WordCleaner;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.infrastructure.adapter.out.persistence.mapper.WordMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WordGetMatchesServiceImpl implements WordGetMatchesService{

    private final ProcessMessageDatabasePort processMessageDatabasePort;
    private final WordCleaner wordCleaner;

    @Override
    public List<Word> getMatches(String frase) {

        List<String> palabras = Arrays.stream(frase.split(" "))
                .filter(s -> !s.isEmpty())
                .map(wordCleaner::clean)
                .toList();

        List<Word> byPalabraIn = processMessageDatabasePort.findByPalabraIn(palabras);

        Map<Word, List<Long>> usuarios = new HashMap<>();

        List<Word> minasActivadas = byPalabraIn.stream().filter(p -> {
            List<Long> idsUsuarios = usuarios.computeIfAbsent(p, k -> new ArrayList<>());
            if(idsUsuarios.contains(p.getUserId()))
                return false;
            idsUsuarios.add(p.getUserId());
            return true;
        }).toList();

        processMessageDatabasePort.deleteAll(minasActivadas);
        return minasActivadas;
    }

}