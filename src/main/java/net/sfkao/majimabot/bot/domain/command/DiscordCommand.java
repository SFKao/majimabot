package net.sfkao.majimabot.bot.domain.command;

import discord4j.core.event.domain.Event;
import discord4j.discordjson.json.ApplicationCommandRequest;

public interface DiscordCommand<T extends Event> extends DiscordEvent<T>{


    ApplicationCommandRequest getCommandRequest();

    String getCommandName();



}
