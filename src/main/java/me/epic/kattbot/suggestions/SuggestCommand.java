package me.epic.kattbot.suggestions;

import me.epic.kattbot.KattBot;
import me.epic.kattbot.Utils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SuggestCommand extends ListenerAdapter {
    private final KattBot plugin;

    public SuggestCommand(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("suggest")) {
            event.deferReply().setEphemeral(true).queue();

            OptionMapping suggestion = event.getOption("suggestion");
            if (suggestion == null) {
                event.getHook().sendMessage("Suggestion not provided").queue();
                return;
            }

            int suggestionID = Utils.getNextSuggestionID(this.plugin.getConnectionPool());
            String userSuggestion = suggestion.getAsString();
            String userName = event.getMember().getEffectiveName();
            String userProfile = event.getUser().getAvatarUrl();

            event.getHook().sendMessage("Your suggestion is in <#983114713955176498>").setEphemeral(true).queue();
            Utils.createSuggestion(this.plugin, suggestionID, userName, userProfile, userSuggestion);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {

            String message = event.getMessage().getContentRaw();
            String userName = event.getMember().getEffectiveName();
            String userProfile = event.getMember().getUser().getAvatarUrl();
            int suggestionID = Utils.getNextSuggestionID(this.plugin.getConnectionPool());

            if (!message.startsWith("?suggest") && !message.startsWith("/suggest"))
                return;

            String userSuggestion = message.substring(message.indexOf(" "));

            Utils.createSuggestion(this.plugin, suggestionID, userName, userProfile, userSuggestion);
            event.getMessage().delete().queue();

            event.getChannel().sendMessage("Your suggestion is in <#983114713955176498>")
                    .queue(result -> result.delete().queueAfter(15, TimeUnit.SECONDS), Throwable::printStackTrace);
        }
    }
}
