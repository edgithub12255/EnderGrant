package EnderGrant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    private final EnderGrant plugin;
    private final Grant grant;

    public Commands(EnderGrant plugin, Grant grant) {
        this.plugin = plugin;
        this.grant = grant;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("grant")) {
            if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
                sendHelpMessage(sender);
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("limits")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    sender.sendMessage(grant.getLimits(player));
                } else {
                    sender.sendMessage("Эту команду может использовать только игрок.");
                }
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("droplimits")) {
                plugin.resetPlayerLimits();
                sender.sendMessage("Лимиты игроков сброшены.");
                return true;
            }

            if (args.length != 3 || !args[0].equalsIgnoreCase("give")) {
                sendHelpMessage(sender);
                return true;
            }

            String targetName = args[1];
            String grantType = args[2];

            if (sender instanceof Player) {
                grant.giveGrant((Player) sender, targetName, grantType);
            } else {
                grant.giveGrant(sender, targetName, grantType);
            }

            return true;
        }

        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        FileConfiguration messages = plugin.getMessages();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("help.header")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("help.grant-give")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("help.grant-limits")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("help.grant-droplimits")));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("grant")) {
            if (args.length == 1) {
                if ("give".startsWith(args[0].toLowerCase())) {
                    suggestions.add("give");
                }
                if ("limits".startsWith(args[0].toLowerCase())) {
                    suggestions.add("limits");
                }
                if ("droplimits".startsWith(args[0].toLowerCase())) {
                    suggestions.add("droplimits");
                }
                if ("help".startsWith(args[0].toLowerCase())) {
                    suggestions.add("help");
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        suggestions.add(player.getName());
                    }
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                FileConfiguration config = plugin.getConfig();
                for (String grant : config.getConfigurationSection("grants").getKeys(false)) {
                    if (grant.toLowerCase().startsWith(args[2].toLowerCase())) {
                        suggestions.add(grant);
                    }
                }
            }
        }

        return suggestions;
    }
}
