package net.sfkao.majimabot.word.infraestructure.blacklist;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.extern.log4j.Log4j2;
import net.sfkao.majimabot.bot.domain.command.DiscordCommand;
import net.sfkao.majimabot.word.domain.Word;
import net.sfkao.majimabot.word.domain.WordRepository;
import net.sfkao.majimabot.word.domain.clean.WordCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
public class WordBlacklistCommandEvent implements DiscordCommand<ChatInputInteractionEvent> {

    @Autowired
    WordRepository wordRepository;

    @Autowired
    WordCleaner wordCleaner;

    @Override
    public ApplicationCommandRequest getCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(getCommandName())
                .description("Añade una palabra a la blacklist")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("palabra")
                        .description("Palabra a añadir")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build()
                ).build();
    }

    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {

        String palabra = event.getOption("palabra").flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asString).orElse("");
        if(palabra.isEmpty())
            return event.reply("Se necesita una palabra jefe");

        palabra = wordCleaner.clean(palabra);

        Word word = new Word()
                .setPalabra(palabra)
                .setUserId(event.getInteraction().getUser().getUserData().id().asLong());

        wordRepository.save(word);

        return event.reply().withEphemeral(true).withContent("Se ha añadido la palabra \""+palabra+"\" a la blacklist");
    }

    @Override
    public Mono<Void> error(Throwable error) {
        log.error(error);
        return Mono.empty();
    }

    @Override
    public String getCommandName() {
        return "blacklist";
    }
}
