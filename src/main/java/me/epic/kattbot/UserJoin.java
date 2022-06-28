package me.epic.kattbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class UserJoin extends ListenerAdapter {
    private final KattBot plugin;

    public UserJoin(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String userId = event.getUser().getId();
        String user = event.getMember().getAsMention();
        String userName = event.getMember().getEffectiveName();
        String userTag = event.getUser().getDiscriminator();
        String userAvatar = event.getUser().getAvatarUrl();
        EmbedBuilder em = new EmbedBuilder();
        em.setAuthor("Member: " + userName + "#" + userTag + " joined", null, userAvatar);
        em.addField("Welcome!", user + " To the server!", false);
        plugin.getDiscordBot().getGuildById(Utils.getGuildId()).getTextChannelById(983070600853606430L).sendMessageEmbeds(em.build()).queue();
        Guild guild = event.getGuild();
        Role member = guild.getRoleById(983848826345488384L);
        UserSnowflake roleMember = event.getMember();
        guild.addRoleToMember(roleMember, member).completeAfter(1, TimeUnit.SECONDS);


    }

}
