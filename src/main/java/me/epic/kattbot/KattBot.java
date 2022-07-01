package me.epic.kattbot;

import me.epic.kattbot.database.DatabaseConnectionPool;
import me.epic.kattbot.moderation.BanCommand;
import me.epic.kattbot.moderation.KickCommand;
import me.epic.kattbot.moderation.StrangerThingsFilter;
import me.epic.kattbot.suggestions.*;
import me.epic.kattbot.UserJoin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class KattBot extends JavaPlugin {
    private JDA discordBot;
    private DatabaseConnectionPool connectionPool;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        setupDatabase();

        String token = getConfig().getString("token");
        if (token.equals("BOTTOKEN")) {
            getServer().getPluginManager().disablePlugin(this);
            getLogger().severe("Epic you twat, add the fucking token");
            return;
        }
        try {
            this.discordBot = JDABuilder.createDefault(token).setActivity(Activity.watching("KattSMP Discord"))
                    //Add event listeners for slash commands
                    .addEventListeners(new SuggestCommand(this)).addEventListeners(new ApproveCommand(this))
                    .addEventListeners(new DenyCommand(this)).addEventListeners(new ImplementedCommand(this))
                    .addEventListeners(new ButtonListener(this)).addEventListeners(new ModalListener(this))
                    .addEventListeners(new KickCommand(this)).addEventListeners(new BanCommand(this))
                    .addEventListeners(new StrangerThingsFilter(this))
                    //User Join listener
                    .addEventListeners(new UserJoin(this))
                    //Join Message Intent
                    .enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_MESSAGES)
                    .build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        discordBot.updateCommands();

        //Command Registering
        Guild testServer = discordBot.getGuildById(983059905806204938L);
        if (testServer != null) {
            testServer.upsertCommand("suggest", "Suggests an idea for the server")
                    .addOption(OptionType.STRING, "suggestion", "your suggestion", true).queue();
            testServer.upsertCommand("approve", "Approves a suggestion")
                    .addOption(OptionType.INTEGER, "number", "selects the suggestion number")
                    .addOption(OptionType.STRING, "reason", "reason of approval").queue();
            testServer.upsertCommand("deny", "Denies a suggestion")
                    .addOption(OptionType.INTEGER, "number", "selects the suggestion number")
                    .addOption(OptionType.STRING, "reason", "reason of denial").queue();
            testServer.upsertCommand("implemented", "Marks a suggestion as implemented")
                    .addOption(OptionType.INTEGER, "number", "selects the suggestion number").queue();
            testServer.upsertCommand("kick", "Kicks a member")
                    .addOption(OptionType.USER, "member", "selects the member", true)
                    .addOption(OptionType.STRING, "reason", "adds a reason").queue();
            testServer.upsertCommand("ban", "Ban a member")
                    .addOption(OptionType.USER, "member", "selects the member", true)
                    .addOption(OptionType.STRING, "reason", "adds a reason").queue();

        }

        this.getCommand("suggest").setExecutor(new MinecraftSuggestCommand(this));
    }

    private void setupDatabase() {
        this.connectionPool = new DatabaseConnectionPool("KattBot", "storage", getDataFolder());

        try (Connection connection = this.connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS suggestions (id INTEGER PRIMARY KEY, user VARCHAR(32), avatar TEXT, suggestion TEXT);")) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public JDA getDiscordBot() {
        return discordBot;
    }

    @Override
    public void onDisable() {
        if (discordBot != null) {
            discordBot.shutdown();
        }
    }

    public DatabaseConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
