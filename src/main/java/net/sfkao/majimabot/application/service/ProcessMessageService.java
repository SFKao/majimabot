package net.sfkao.majimabot.application.service;

import net.sfkao.majimabot.application.port.in.ProcessMessagePort;
import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.application.service.word.WordGetMatchesService;
import net.sfkao.majimabot.domain.MessageEvent;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.random.RandomGenerator;

@Service
@Qualifier("processMessageService")
public class ProcessMessageService implements ProcessMessagePort {

    private final WordGetMatchesService wordGetMatchesService;
    private final WordBlackListDatabasePort wordBlackListDatabasePort;

    private final RandomGenerator random;

    @Value("${probabilidadshiny}")
    private int shinyChance = 4096;


    @Autowired
    public ProcessMessageService(WordGetMatchesService wordGetMatchesService,
                                 WordBlackListDatabasePort wordBlackListDatabasePort,// Inyectamos el puerto
                                 RandomGenerator random
                                 ) {
        this.wordGetMatchesService = wordGetMatchesService;
        this.wordBlackListDatabasePort = wordBlackListDatabasePort; // Guardamos el puerto en una variable
        this.random = random;
    }

    @Override
    public MessageEvent processMessage(MessageEvent event) throws UserNotFoundException {
        String content = event.getContent();
        List<Word> matches = wordGetMatchesService.getMatches(content);

        long idComeminas = event.getIdUsuario();

        if (matches.isEmpty()) {
            if (random.nextInt(shinyChance) == 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1);
                event.setMessageResponse("<@" + idComeminas + "> enhorabuena, te has comido una mina shiny, gilipollas")
                        .setBanUntil(calendar.toInstant());
            }
            return event;
        }

        // Llamamos al puerto externo para incrementar las minas
        wordBlackListDatabasePort.incrementMines(idComeminas, matches.size());


        // Manejamos los puntos de los usuarios que tienen palabras coincidentes
        Map<Long, Integer> userPoints = new HashMap<>();
        matches.forEach(m -> {
            userPoints.merge(m.getUserId(), 1, Integer::sum);
        });

        userPoints.forEach(wordBlackListDatabasePort::incrementPoints); // Usamos el puerto para incrementar los puntos

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, matches.size());

        event.setBanUntil(calendar.toInstant());
        event.setMatches(matches);

        return event;
    }
}