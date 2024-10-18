package net.sfkao.majimabot.word.infraestructure.listener;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.extern.log4j.Log4j2;
import net.sfkao.majimabot.bot.domain.command.DiscordEvent;
import net.sfkao.majimabot.usuario.application.incrementMinas.UsuarioIncrementMinas;
import net.sfkao.majimabot.usuario.application.incrementPuntos.UsuarioIncrementPuntos;
import net.sfkao.majimabot.word.application.getmatches.WordGetMatchesService;
import net.sfkao.majimabot.word.domain.Word;
import net.sfkao.majimabot.word.domain.clean.WordCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.*;

@Controller
@Log4j2
public class WordBlacklistListener implements DiscordEvent<MessageCreateEvent> {

    @Autowired
    private WordGetMatchesService wordGetMatchesService;

    @Autowired
    private UsuarioIncrementMinas usuarioIncrementMinas;

    @Autowired
    private UsuarioIncrementPuntos usuarioIncrementPuntos;

    @Autowired
    private WordCleaner wordCleaner;

    @Value("${probabilidadshiny}")
    private int shinyChance;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        String content = event.getMessage().getContent();
        if(event.getMember().isEmpty() || content.isEmpty())
            return Mono.empty();

        List<String> list = Arrays.stream(content.split(" ")).map(wordCleaner::clean).toList();
        List<Word> matches = wordGetMatchesService.getMatches(list);

        long idComeminas = event.getMember().get().getId().asLong();

        if(matches.isEmpty()) {
            if(new Random().nextInt(shinyChance)==1) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1);
                Mono<Void> then = event.getMember().get().edit().withCommunicationDisabledUntilOrNull(calendar.toInstant()).then();
                return event.getMessage().getChannel().
                        flatMap(channel -> channel.createMessage("<@"+idComeminas+"> enhorabuena, te has comido una mina shiny, gilipollas"))
                        .then(then);
            }
            return Mono.empty();
        }

        usuarioIncrementMinas.incrementMinas(idComeminas, matches.size());

        Map<Long, Integer> userPoints = new HashMap<>();
        matches.forEach(m -> {
            userPoints.compute(m.getUserId(), (key, data) -> {
                if(data==null)
                    return 1;
                return data++;
                });
        });

        userPoints.forEach((key, value) -> usuarioIncrementPuntos.incrementPuntos(key, value));

        StringBuilder sb = new StringBuilder();
        sb.append("<@").append(idComeminas).append("> ha sido silenciado por decir [");
        for(Word w : matches){
            sb.append("'").append(w.getPalabra()).append("' de ").append(event.getClient().getUserById(Snowflake.of(w.getUserId())).block().getUsername()).append(", ");
        }
        sb.deleteCharAt(sb.length()-1).deleteCharAt(sb.length()-1).append("]");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, matches.size());

        Mono<Void> then = event.getMember().get().edit().withCommunicationDisabledUntilOrNull(calendar.toInstant()).onErrorResume(error -> {
            if(!(error instanceof PermissionDeniedDataAccessException))
                return Mono.empty();
            return Mono.error(error);
        }).then();


        return event.getMessage().getChannel().
                flatMap(channel -> channel.createMessage(sb.toString()))
                .then(then);

    }

    @Override
    public Mono<Void> error(Throwable error) {
        log.error("Error leyendo un mensaje ",error);
        return Mono.empty();
    }
}
