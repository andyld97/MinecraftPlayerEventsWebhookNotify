package code.a.software;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    private Webhook wh;

    private FileConfiguration config;

    private PlayerEventListener listener;

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
            config.set("playerDeathNotify", true);
            config.set("playerChatNotify", false);
            config.set("playerChatMessage", "[%1$s]: %2$s");
            config.set("playerChatName", "");
            config.set("playerChatEndpoint", "");
            saveConfig();
        }

        listener = new PlayerEventListener(this, wh, config);
        Bukkit.getPluginManager().registerEvents(listener,this);

        listener.Start();
    }

    @Override
    public void onDisable() {
        listener.Stop();
    }
}