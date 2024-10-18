package net.sfkao.majimabot.word.domain.clean;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class WordClenaerImpl implements WordCleaner{

    @Override
    public String clean(String palabra) {

        palabra = Normalizer.normalize(palabra, Normalizer.Form.NFKD);
        palabra = palabra.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return palabra.trim();
    }
}
