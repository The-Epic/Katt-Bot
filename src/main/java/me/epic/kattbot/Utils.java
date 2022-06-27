package me.epic.kattbot;

import me.epic.kattbot.database.DatabaseConnectionPool;
import me.epic.kattbot.suggestions.SuggestionData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static SuggestionData getSuggestion(DatabaseConnectionPool pool, int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM suggestions WHERE id = ?;")) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int index = resultSet.getInt("id");
                String user = resultSet.getString("user");
                String avatar = resultSet.getString("avatar");
                String suggestion = resultSet.getString("suggestion");

                return new SuggestionData(index, user, avatar, suggestion);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getNextSuggestionID(DatabaseConnectionPool pool) {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("SELECT MAX(id) AS max_id FROM suggestions")) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static void createSuggestion(KattBot plugin, int suggestionID, String userName, String userProfile,
                                        String userSuggestion) {
        EmbedBuilder em = new EmbedBuilder();
        em.setAuthor(userName, null, userProfile);
        em.setTitle(" ", null);
        em.setColor(0x4EFF31);
        em.addField("New suggestion! #" + suggestionID, userSuggestion, false);
        plugin.getDiscordBot().getGuildById(Utils.getGuildId()).getTextChannelById(983114713955176498L)
                .sendMessageEmbeds(em.build()).setActionRow(
                        Button.success("approve", "Approve"),
                        Button.danger("deny", "Deny"),
                        Button.primary("implemented", "Implement"))
                .queue(message -> {
                    message.addReaction(":upvote:983098384560816179").queue();
                    message.addReaction(":downvote:983098418106875984").queue();
                    message.createThreadChannel("Suggestion-" + suggestionID).queue();
                });


        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = plugin.getConnectionPool().getConnection();
                 PreparedStatement statement = connection
                         .prepareStatement("INSERT INTO suggestions (user, avatar, suggestion) VALUES (?,?,?);")) {
                statement.setString(1, userName);
                statement.setString(2, userProfile);
                statement.setString(3, userSuggestion);

                statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static long getGuildId() {
        return 983059905806204938L;
    }
}