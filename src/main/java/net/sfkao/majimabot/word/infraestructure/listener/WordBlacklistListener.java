package net.sfkao.majimabot.word.infraestructure.listener;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.extern.log4j.Log4j2;
import net.sfkao.majimabot.bot.domain.command.DiscordEvent;
import net.sfkao.majimabot.word.application.getmatches.WordGetMatchesService;
import net.sfkao.majimabot.word.domain.Word;
import net.sfkao.majimabot.word.domain.clean.WordCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
public class WordBlacklistListener implements DiscordEvent<MessageCreateEvent> {

    @Autowired
    private WordGetMatchesService wordGetMatchesService;

    @Autowired
    private WordCleaner wordCleaner;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        String content = event.getMessage().getContent();
        List<String> list = Arrays.stream(content.split(" ")).map(wordCleaner::clean).toList();
        List<Word> matches = wordGetMatchesService.getMatches(list);

        if(!matches.isEmpty())
            return event.getMessage().getChannel().
                    flatMap(channel -> channel.createMessage(matches.toString()))
                    .then();

        return Mono.empty();
    }

    @Override
    public Mono<Void> error(Throwable error) {
        log.error(error);
        return Mono.empty();
    }
}
