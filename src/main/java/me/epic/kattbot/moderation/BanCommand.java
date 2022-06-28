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

public class BanCommand extends ListenerAdapter {
    private final KattBot plugin;

    public BanCommand(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Role staff = event.getJDA().getGuildById(Utils.getGuildId()).getRoleById(983059905806204941L);
        if (event.getName().equals("ban") && event.getMember().getRoles().contains(staff)) {

            event.deferReply().queue();

            OptionMapping member = event.getOption("member");
            OptionMapping reason = event.getOption("reason");
            OptionMapping deletedays = event.getOption("deletedays");

            String banReason;
            if (reason == null) {
                banReason = "No reason specified";
            } else {
                banReason = reason.getAsString();
            }
            int delDays;
            if (deletedays == null && deletedays.equals("0")) {
                delDays = 0;
            } else {
                delDays = deletedays.getAsInt();
            }

            String userPing = member.getAsUser().getAsMention();
            UserSnowflake userId = member.getAsUser();
            String user = member.getAsUser().getName();
            String avatar = member.getAsUser().getEffectiveAvatarUrl();
            String whoBanned = event.getMember().getEffectiveName();
            String whoBannedAvatar = event.getMember().getEffectiveAvatarUrl();
            String guildName = event.getGuild().getName();
            EmbedBuilder em = new EmbedBuilder();
            em.setTitle("Member Banned", null);
            em.setDescription("User: " + userPing + " Was banned");
            em.setAuthor(user, null, avatar);
            em.addField("Reason:", banReason, false);
            EmbedBuilder em1 = new EmbedBuilder();
            em1.setAuthor(whoBanned, null, whoBannedAvatar);
            em1.setTitle("You got banned from " + guildName);
            em1.setDescription("Your ban is permanent, so you are unable to join back");
            em1.addField("Your ban reason is:", banReason, false);
            try {
                member.getAsUser().openPrivateChannel().complete().sendMessageEmbeds(em1.build()).queue();
                member.getAsUser().getJDA().getGuildById(Utils.getGuildId()).ban(userId, delDays, banReason).completeAfter(10, TimeUnit.MILLISECONDS);
            } catch (ErrorResponseException ignored) {
                throw ignored;
            }
            event.getHook().sendMessageEmbeds(em.build()).queue();
        }
    }
}