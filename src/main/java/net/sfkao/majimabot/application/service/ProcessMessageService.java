package net.sfkao.majimabot.application.service;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import net.sfkao.majimabot.application.port.in.ProcessMessagePort;
import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.application.service.word.WordGetMatchesService;
import net.sfkao.majimabot.application.util.WordCleaner;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Qualifier("processMessageService")
public class ProcessMessageService implements ProcessMessagePort {

    private final WordGetMatchesService wordGetMatchesService;
    private final WordBlackListDatabasePort wordBlackListDatabasePort;
    private final WordCleaner wordCleaner;
    private final int shinyChance;

    @Autowired
    public ProcessMessageService(WordGetMatchesService wordGetMatchesService,
                                 WordBlackListDatabasePort wordBlackListDatabasePort, // Inyectamos el puerto
                                 WordCleaner wordCleaner,
                                 @Value("${probabilidadshiny}") int shinyChance) {
        this.wordGetMatchesService = wordGetMatchesService;
        this.wordBlackListDatabasePort = wordBlackListDatabasePort; // Guardamos el puerto en una variable
        this.wordCleaner = wordCleaner;
        this.shinyChance = shinyChance;
    }

    @Override
    public Mono<Void> processMessage(MessageCreateEvent event) throws UserNotFoundException {
        String content = event.getMessage().getContent();
        if (event.getMember().isEmpty() || content.isEmpty()) {
            return Mono.empty();
        }

        List<String> list = Arrays.stream(content.split(" "))
                .map(wordCleaner::clean)
                .toList();
        List<Word> matches = wordGetMatchesService.getMatches(list);

        long idComeminas = event.getMember().get().getId().asLong();

        if (matches.isEmpty()) {
            if (new Random().nextInt(shinyChance) == 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1);
                Mono<Void> then = event.getMember().get().edit()
                        .withCommunicationDisabledUntilOrNull(calendar.toInstant()).then();
                return event.getMessage().getChannel()
                        .flatMap(channel -> channel.createMessage("<@" + idComeminas + "> enhorabuena, te has comido una mina shiny, gilipollas"))
                        .then(then);
            }
            return Mono.empty();
        }

        // Llamamos al puerto externo para incrementar las minas
        wordBlackListDatabasePort.incrementMines(idComeminas, matches.size());

        // Manejamos los puntos de los usuarios que tienen palabras coincidentes
        Map<Long, Integer> userPoints = new HashMap<>();
        matches.forEach(m -> {
            userPoints.compute(m.getUserId(), (key, data) -> (data == null) ? 1 : data++);
        });

        userPoints.forEach(wordBlackListDatabasePort::incrementPoints); // Usamos el puerto para incrementar los puntos

        StringBuilder sb = new StringBuilder();
        sb.append("<@").append(idComeminas).append("> ha sido silenciado por decir [");
        for (Word w : matches) {
            sb.append("'").append(w.getPalabra()).append("' de ")
                    .append(event.getClient().getUserById(Snowflake.of(w.getUserId())).block().getUsername()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append("]");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, matches.size());

        Mono<Void> then = event.getMember().get().edit()
                .withCommunicationDisabledUntilOrNull(calendar.toInstant())
                .onErrorResume(error -> {
                    if (!(error instanceof PermissionDeniedDataAccessException)) {
                        return Mono.empty();
                    }
                    return Mono.error(error);
                }).then();

        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage(sb.toString()))
                .then(then);
    }
}