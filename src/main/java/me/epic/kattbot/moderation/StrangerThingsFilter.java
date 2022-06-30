package me.epic.kattbot.moderation;

import me.epic.kattbot.KattBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StrangerThingsFilter extends ListenerAdapter {
    private final KattBot plugin;

    public StrangerThingsFilter(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && event.getMessage().getContentRaw().toLowerCase().contains("stranger things")) {
            event.getMessage().delete().queue();
        }
    }
    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.isFromType(ChannelType.TEXT) && event.getMessage().getContentRaw().toLowerCase().contains("stranger things")) {
            event.getMessage().delete().queue();
        }
    }
}
