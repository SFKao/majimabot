package net.sfkao.majimabot.word.application.getmatches;

import com.austinv11.servicer.Service;
import net.sfkao.majimabot.word.domain.Word;
import net.sfkao.majimabot.word.domain.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordGetMatchesServiceImpl implements WordGetMatchesService{

    @Autowired
    WordRepository wordRepository;

    @Override
    public List<Word> getMatches(List<String> palabras) {

        List<Word> byPalabraIn = wordRepository.findByPalabraIn(palabras);

        List<Long> idsUsuarios = new ArrayList<>();

        List<Word> minasActivadas = byPalabraIn.stream().filter(p -> {
            if (idsUsuarios.contains(p.getUserId()))
                return false;
            idsUsuarios.add(p.getUserId());
            return true;
        }).toList();

        wordRepository.deleteAll(minasActivadas);
        return minasActivadas;
    }

}
