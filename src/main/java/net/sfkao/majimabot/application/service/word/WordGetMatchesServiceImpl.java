package net.sfkao.majimabot.application.service.word;

import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.out.ProcessMessageDatabasePort;
import net.sfkao.majimabot.domain.Word;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WordGetMatchesServiceImpl implements WordGetMatchesService{

    private final ProcessMessageDatabasePort processMessageDatabasePort;

    @Override
    public List<Word> getMatches(List<String> palabras) {

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