package net.sfkao.majimabot.infrastructure.adapter.in;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.extern.log4j.Log4j2;
import net.sfkao.majimabot.application.port.in.UpdateBlackListPort;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.InvalidPalabraException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
public class WordBlacklistCommandEventAdapter implements DiscordCommandEvent<ChatInputInteractionEvent>, UpdateBlackListPort {

    private final UpdateBlackListPort updateBlackListPort;

    public WordBlacklistCommandEventAdapter(@Qualifier("updateBlackListService") UpdateBlackListPort updateBlackListPort) {
        this.updateBlackListPort = updateBlackListPort;
    }

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

        String content = event.getOption("palabra")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        Word word = null;
        try {
            word = this.processMessage(new Word(null, content, event.getInteraction().getUser().getUserData().id().asLong()));
        } catch (InvalidPalabraException e) {
            return event.reply().withEphemeral(true).withContent("Se necesita una palabra").then();
        }

        return event.reply().withEphemeral(true).withContent("Se ha añadido la palabra \"" + word.getPalabra() + "\" a la blacklist").then();

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

    @Override
    public Word processMessage(Word palabra) throws InvalidPalabraException {
        return this.updateBlackListPort.processMessage(palabra);
    }
}
