package EnderGrant;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class EnderGrant extends JavaPlugin {
    private Grant grant;
    private FileConfiguration messagesConfig;
    private FileConfiguration playersConfig;
    private File messagesFile;
    private File playersFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMessagesConfig();
        loadPlayersConfig();

        grant = new Grant(this);
        Commands commands = new Commands(this, grant);
        getCommand("grant").setExecutor(commands);
        getCommand("grant").setTabCompleter(commands);
        getLogger().info("EnderGrant включен!");
    }

    @Override
    public void onDisable() {
        savePlayersConfig();
        getLogger().info("EnderGrant выключен!");
    }

    public Grant getGrant() {
        return grant;
    }

    public FileConfiguration getMessages() {
        return messagesConfig;
    }

    public FileConfiguration getPlayersConfig() {
        return playersConfig;
    }

    private void loadMessagesConfig() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private void loadPlayersConfig() {
        playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            playersFile.getParentFile().mkdirs();
            try {
                playersFile.createNewFile();
                initializePlayerLimits();
            } catch (IOException e) {
                getLogger().severe("Не удалось создать players.yml!");
                e.printStackTrace();
            }
        }

        playersConfig = YamlConfiguration.loadConfiguration(playersFile);
    }

    public void savePlayersConfig() {
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            getLogger().severe("Не удалось сохранить players.yml!");
            e.printStackTrace();
        }
    }

    private void initializePlayerLimits() {
        FileConfiguration config = getConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("endergrant.use")) {
                String playerName = player.getName();
                for (String grant : config.getConfigurationSection("limits.ender").getKeys(false)) {
                    int limit = config.getInt("limits.ender." + grant);
                    playersConfig.set("limits." + playerName + "." + grant, limit);
                }
            }
        }
        savePlayersConfig();
    }

    public void resetPlayerLimits() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("endergrant.use")) {
                String playerName = player.getName();
                for (String grant : getConfig().getConfigurationSection("limits.ender").getKeys(false)) {
                    int limit = getConfig().getInt("limits.ender." + grant);
                    playersConfig.set("limits." + playerName + "." + grant, limit);
                }
            }
        }
        savePlayersConfig();
    }
}
