package me.epic.kattbot.suggestions;

import me.epic.kattbot.KattBot;
import me.epic.kattbot.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalListener extends ListenerAdapter {
    private final KattBot plugin;

    public ModalListener(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("approve-modal")) {

            String number = event.getValue("approve-int").getAsString();
            String reason = event.getValue("approve-reason").getAsString();

            int suggestionNumber = Integer.parseInt(number);
            String acceptReason = reason;
            String userName = event.getMember().getEffectiveName();
            String userProfile = event.getMember().getEffectiveAvatarUrl();
            SuggestionData suggestion = Utils.getSuggestion(this.plugin.getConnectionPool(), suggestionNumber);


            EmbedBuilder em = new EmbedBuilder();
            em.setAuthor(userName + " Approved suggestion #" + suggestionNumber, null, userProfile);
            em.setTitle(" ", null);
            em.setColor(0x22ff00);
            em.addField("Approved Suggestion:", suggestion.getSuggestion(), false);
            em.addField(userName + " Approved because:", acceptReason, false);

            event.getHook().getJDA().getGuildById(Utils.getGuildId()).getTextChannelById(983115701516660736L).sendMessageEmbeds(em.build()).queue();
            event.reply("Suggestion #" + suggestionNumber + " Has been accepted").setEphemeral(true).queue();
        }
        if (event.getModalId().equals("deny-modal")) {

            String number = event.getValue("deny-int").getAsString();
            String reason = event.getValue("deny-reason").getAsString();

            int suggestionNumber = Integer.parseInt(number);
            String denyReason = reason;
            String userName = event.getMember().getEffectiveName();
            String userProfile = event.getMember().getEffectiveAvatarUrl();
            SuggestionData suggestion = Utils.getSuggestion(this.plugin.getConnectionPool(), suggestionNumber);


            EmbedBuilder em = new EmbedBuilder();
            em.setAuthor(userName + " Denied suggestion #" + suggestionNumber, null, userProfile);
            em.setTitle(" ", null);
            em.setColor(0xfc0303);
            em.addField("Denied Suggestion:", suggestion.getSuggestion(), false);
            em.addField(userName + " Denied because:", denyReason, false);
            event.getHook().getJDA().getGuildById(Utils.getGuildId()).getTextChannelById(983115701516660736L).sendMessageEmbeds(em.build()).queue();
            event.reply("Suggestion #" + suggestionNumber + " Has been denied").setEphemeral(true).queue();
        }
        if (event.getModalId().equals("implement-modal")) {

            String number = event.getValue("implement-int").getAsString();

            int suggestionNumber = Integer.parseInt(number);
            String userName = event.getMember().getEffectiveName();
            String userProfile = event.getMember().getEffectiveAvatarUrl();
            SuggestionData suggestion = Utils.getSuggestion(this.plugin.getConnectionPool(), suggestionNumber);


            EmbedBuilder em = new EmbedBuilder();
            em.setAuthor(userName + " Implemented suggestion #" + suggestionNumber, null, userProfile);
            em.setTitle(" ", null);
            em.setColor(0x22ff00);
            em.addField("Implemented Suggestion:", suggestion.getSuggestion(), false);
            event.getHook().getJDA().getGuildById(Utils.getGuildId()).getTextChannelById(983115701516660736L).sendMessageEmbeds(em.build()).queue();
            event.reply("Suggestion #" + suggestionNumber + " Has been implemented").setEphemeral(true).queue();
        }
    }
}
