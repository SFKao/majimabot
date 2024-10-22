package net.sfkao.majimabot.application.service.word;

import net.sfkao.majimabot.domain.Word;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordGetMatchesService {

    List<Word> getMatches(List<String> palabras);

}
