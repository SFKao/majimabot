package net.sfkao.majimabot.word.application.getmatches;

import net.sfkao.majimabot.word.domain.Word;
import net.sfkao.majimabot.word.domain.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WordGetMatchesServiceImpl implements WordGetMatchesService{

    @Autowired
    WordRepository wordRepository;

    @Override
    public List<Word> getMatches(List<String> palabras) {

        List<Word> byPalabraIn = wordRepository.findByPalabraIn(palabras);

        Map<Word, List<Long>> usuarios = new HashMap<>();

        List<Word> minasActivadas = byPalabraIn.stream().filter(p -> {
            List<Long> idsUsuarios = usuarios.computeIfAbsent(p, k -> new ArrayList<>());
            if(idsUsuarios.contains(p.getUserId()))
                return false;
            idsUsuarios.add(p.getUserId());
            return true;
        }).toList();

        wordRepository.deleteAll(minasActivadas);
        return minasActivadas;
    }

}
