package code.a.software;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerOnLeaveListener implements Listener {
    private Main main;
    private Webhook wh;
    private FileConfiguration config;

    public PlayerOnLeaveListener(Main main, Webhook wh, FileConfiguration config)
    {
        this.main = main;
        this.wh = wh;
        this.config = config;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        String serverName = main.getServer().getName();

        String leaveMessage = config.getString("leaveMessage", "Player %s left the game!");
        String message = String.format(leaveMessage, ev.getPlayer().getDisplayName());

        if (config.getBoolean("appendServerName"))
            message += " (" + serverName + ")";

        wh.NotifyWebHook(message);
    }
}
