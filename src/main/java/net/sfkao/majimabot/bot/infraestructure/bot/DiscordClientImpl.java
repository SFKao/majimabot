package net.sfkao.majimabot.bot.infraestructure.bot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import net.sfkao.majimabot.bot.domain.command.DiscordCommand;
import net.sfkao.majimabot.bot.domain.command.DiscordEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DiscordClientImpl  implements DiscordClient{

    @Value("${token}")
    private String token;

    @Value("${guildId}")
    private long guildId;

    @Override
    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<DiscordEvent<T>> events, List<DiscordCommand<T>> commands) {

        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();

        // Get our application's ID
        long applicationId = client.getRestClient().getApplicationId().block();

        client.getRestClient()
                .getApplicationService()
                .bulkOverwriteGuildApplicationCommand( applicationId , guildId, commands.stream().map(DiscordCommand::getCommandRequest).collect(Collectors.toList()))
                .subscribe();


        for(DiscordEvent<T> event : events) {
            client.on(event.getEventType())
                    .flatMap(event::execute)
                    .onErrorResume(event::error)
                    .subscribe();
        }

        return client;

    }
}
