package me.epic.kattbot.suggestions;

import me.epic.kattbot.KattBot;
import me.epic.kattbot.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinecraftSuggestCommand implements CommandExecutor {
    private final KattBot plugin;

    public MinecraftSuggestCommand(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "You must include a suggestion to suggest something");
            return true;
        }

        int suggestionID = Utils.getNextSuggestionID(this.plugin.getConnectionPool());
        String userSuggestion = String.join(" ", args);
        String userName = ChatColor.stripColor(player.getDisplayName());
        String userProfile = "https://heads.discordsrv.com/head.png?uuid=" + player.getUniqueId() + "&overlay";

        sender.sendMessage(ChatColor.GREEN + "Your suggestion has been added as #" + suggestionID);

        Utils.createSuggestion(this.plugin, suggestionID, userName, userProfile, userSuggestion);
        return true;
    }
}