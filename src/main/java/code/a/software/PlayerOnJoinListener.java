package code.a.software;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
public class PlayerOnJoinListener implements Listener {
    private Main main;
    private Webhook wh;

    private FileConfiguration config;

    public PlayerOnJoinListener(Main main, Webhook wh, FileConfiguration config)
    {
        this.main = main;
        this.wh = wh;
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        String serverName = main.getServer().getName();

        String joinMessage = config.getString("joinMessage", "Player %s joined the game!");
        String message = String.format(joinMessage, ev.getPlayer().getDisplayName());

        if (config.getBoolean("appendServerName"))
            message += " (" + serverName + ")";

        wh.NotifyWebHook(message);
    }
}