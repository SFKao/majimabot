package net.sfkao.majimabot.word.application.getmatches;

import com.austinv11.servicer.Service;
import net.sfkao.majimabot.word.domain.Word;

import java.util.List;

@Service
public interface WordGetMatchesService {

    List<Word> getMatches(List<String> palabras);

}
