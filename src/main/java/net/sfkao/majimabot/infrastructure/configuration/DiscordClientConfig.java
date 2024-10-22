package net.sfkao.majimabot.infrastructure.configuration;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import net.sfkao.majimabot.infrastructure.adapter.in.DiscordCommandEvent;
import net.sfkao.majimabot.infrastructure.adapter.in.DiscordEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.util.List;

public interface DiscordClientConfig {

    @Bean
    <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<DiscordEvent<T>> events, List<DiscordCommandEvent<T>> commands);

}
