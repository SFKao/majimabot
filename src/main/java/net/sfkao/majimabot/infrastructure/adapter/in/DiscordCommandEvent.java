package net.sfkao.majimabot.infrastructure.adapter.in;

import discord4j.core.event.domain.Event;
import discord4j.discordjson.json.ApplicationCommandRequest;

public interface DiscordCommandEvent<T extends Event> extends DiscordEvent<T>{

    ApplicationCommandRequest getCommandRequest();

    String getCommandName();
}
