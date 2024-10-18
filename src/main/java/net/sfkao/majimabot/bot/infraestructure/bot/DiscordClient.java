package net.sfkao.majimabot.bot.infraestructure.bot;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import net.sfkao.majimabot.bot.domain.command.DiscordCommand;
import net.sfkao.majimabot.bot.domain.command.DiscordEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface DiscordClient {

    @Bean
    <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<DiscordEvent<T>> events, List<DiscordCommand<T>> commands);

}
