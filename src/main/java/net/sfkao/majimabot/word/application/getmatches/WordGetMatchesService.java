package net.sfkao.majimabot.word.application.getmatches;

import net.sfkao.majimabot.word.domain.Word;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordGetMatchesService {

    List<Word> getMatches(List<String> palabras);

}
