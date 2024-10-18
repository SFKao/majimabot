package net.sfkao.majimabot.word.domain.clean;

import org.springframework.stereotype.Component;

@Component
public interface WordCleaner {

    String clean(String palabra);

}
