package net.sfkao.majimabot.application.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class WordCleanerImpl implements WordCleaner{

    @Override
    public String clean(String palabra) {

        palabra = Normalizer.normalize(palabra, Normalizer.Form.NFKD);
        palabra = palabra.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return palabra.trim();
    }
}
