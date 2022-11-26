package code.a.software;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    // Initialize your webhook instance
    private Webhook wh;

    private FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();

        wh = new Webhook(config.getString("prefix", "[MC-SRV]"), config.getString("webHookUrl"));

        if (!config.getBoolean("firstStart")) {
            config.set("firstStart", true);
            config.set("prefix", "[MC-SRV]");
            config.set("joinMessage", "Player %s joined the game!");
            config.set("leaveMessage", "Player %s left the game!");
            config.set("webHookUrl", "http://your.url?param={0}");
            saveConfig();
        }

        Bukkit.getPluginManager().registerEvents(new PlayerOnJoinListener(this, wh, config),this);
        Bukkit.getPluginManager().registerEvents(new PlayerOnLeaveListener(this, wh, config),this);
    }

    @Override
    public void onDisable() {
        // nothing
    }
}