package me.epic.kattbot.suggestions;

import me.epic.kattbot.KattBot;
import me.epic.kattbot.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class ImplementedCommand extends ListenerAdapter {
    private final KattBot plugin;

    public ImplementedCommand(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("implemented")) {
            event.deferReply().setEphemeral(true).queue();

            OptionMapping number = event.getOption("number");
            if (number == null) {
                event.getHook().sendMessage("You must add a suggestion number to implement it!").queue();
                return;
            }

            int suggestionNumber = number.getAsInt();
            String userName = event.getMember().getEffectiveName();
            String userProfile = event.getMember().getEffectiveAvatarUrl();
            SuggestionData suggestion = Utils.getSuggestion(this.plugin.getConnectionPool(), suggestionNumber);


            EmbedBuilder em = new EmbedBuilder();
            em.setAuthor(userName + " Implemented suggestion #" + suggestionNumber, null, userProfile);
            em.setTitle(" ", null);
            em.setColor(0x22ff00);
            em.addField("Implemented Suggestion:", suggestion.getSuggestion(), false);
            event.getHook().getJDA().getGuildById(983059905806204938L).getTextChannelById(983115701516660736L).sendMessageEmbeds(em.build()).queue();
            event.getHook().sendMessage("Suggestion #" + suggestionNumber + " Has been implemented").setEphemeral(true).queue();
        }

    }
}