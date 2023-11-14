package code.a.software;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class PlayerEventListener implements Listener, ActionListener {
    private final Main main;
    private final Webhook wh;

    private final FileConfiguration config;

    private final Timer timer;

    public PlayerEventListener(Main main, Webhook wh, FileConfiguration config)
    {
        this.main = main;
        this.wh = wh;
        this.config = config;

        timer = new Timer(5000, this);
    }

    private boolean isNullOrEmpty(String value)
    {
        return value == null || value.length() == 0;
    }

    private boolean IsChatReceiverEnabled() {
        if (isNullOrEmpty(config.getString("playerChatEndpoint")) || isNullOrEmpty(config.getString("playerChatName")))
            return false;

        return true;
    }

    public void Start()
    {
        if (IsChatReceiverEnabled())
            timer.start();
    }

    public void Stop()
    {
        if (IsChatReceiverEnabled())
            timer.stop();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        String serverName = main.getServer().getName();

        String joinMessage = config.getString("joinMessage", "Player %s joined the game!");
        String message = String.format(joinMessage, ev.getPlayer().getDisplayName());

        if (config.getBoolean("appendServerName"))
            message += " (" + serverName + ")";

        wh.NotifyWebHook(message, 0);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        String serverName = main.getServer().getName();

        String leaveMessage = config.getString("leaveMessage", "Player %s left the game!");
        String message = String.format(leaveMessage, ev.getPlayer().getDisplayName());

        if (config.getBoolean("appendServerName"))
            message += " (" + serverName + ")";

        wh.NotifyWebHook(message, 0);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!config.getBoolean("playerChatNotify"))
            return;

        String playerMessage = event.getMessage();
        String player = event.getPlayer().getDisplayName();

        String format = config.getString("playerChatMessage", "[%1$s]: %2$s");

        String generatedMessage = String.format(format, player, playerMessage);
        wh.NotifyWebHook(generatedMessage, 1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!config.getBoolean("playerDeathNotify"))
            return;

        String deathMessage = event.getDeathMessage();
        wh.NotifyWebHook(deathMessage, 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String url = config.getString("playerChatEndpoint") + wh.getPrefix();
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("content-type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // If there is a valid answer with content, post it to the chat
            if (response.statusCode() == 200 && response.body().length() > 0) {
                String name = response.headers().firstValue("X-Name").get();
                if (name == null || name.length() == 0)
                    name = config.getString("playerChatName");

                Bukkit.broadcastMessage(ChatColor.YELLOW + name  + ": " + response.body());
            }
        }
        catch (Exception ignored)  {
        }
    }
}