package net.sfkao.majimabot.application.util;

import org.springframework.stereotype.Component;

@Component
public interface WordCleaner {

    String clean(String palabra);

}
