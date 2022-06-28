package me.epic.kattbot.moderation;

import me.epic.kattbot.KattBot;
import me.epic.kattbot.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class KickCommand extends ListenerAdapter {
    private final KattBot plugin;

    public KickCommand(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Role staff = event.getJDA().getGuildById(Utils.getGuildId()).getRoleById(983059905806204941L);
        if (event.getName().equals("kick") && event.getMember().getRoles().contains(staff)) {

            event.deferReply().queue();

            OptionMapping member = event.getOption("member");
            OptionMapping reason = event.getOption("reason");

            String kickReason;
            if (reason == null) {
                kickReason = "No reason specified";
            } else {
                kickReason = reason.getAsString();
            }

            String userPing = member.getAsUser().getAsMention();
            UserSnowflake userId = member.getAsUser();
            String user = member.getAsUser().getName();
            String avatar = member.getAsUser().getEffectiveAvatarUrl();
            String whoKicked = event.getMember().getEffectiveName();
            String whoKickedAvatar = event.getMember().getEffectiveAvatarUrl();
            String guildName = event.getGuild().getName();
            EmbedBuilder em = new EmbedBuilder();
            em.setTitle("Member Kicked", null);
            em.setDescription("User: " + userPing + " Was kicked");
            em.setAuthor(user, null, avatar);
            em.addField("Reason:", kickReason, false);
            EmbedBuilder em1 = new EmbedBuilder();
            em1.setAuthor(whoKicked, null, whoKickedAvatar);
            em1.setTitle("You got kicked from " + guildName);
            em1.setDescription("You can join back at http://discord.kattsmp.com");
            em1.addField("Your kick reason is:", kickReason, false);
            try {
                member.getAsUser().openPrivateChannel().complete().sendMessageEmbeds(em1.build()).queue();
                member.getAsUser().getJDA().getGuildById(Utils.getGuildId()).kick(userId, kickReason).completeAfter(10, TimeUnit.MILLISECONDS);
            } catch (ErrorResponseException ignored) {
                throw ignored;
            }
            event.getHook().sendMessageEmbeds(em.build()).queue();
        }
    }
}
