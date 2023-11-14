package code.a.software.playerevents;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// The value here should match an entry in the META-INF/mods.toml file
@Mod.EventBusSubscriber(modid = PlayerEventsWebhookNotify.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(PlayerEventsWebhookNotify.MODID)
public class PlayerEventsWebhookNotify implements ActionListener
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "playereventswebhooknotify";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static Webhook wh;

    private Timer timer;

    private MinecraftServer server;

    private  Config config;

    public PlayerEventsWebhookNotify()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Load config file
        config = Config.loadConfig("config/PlayerWebhookNotify.json");

        // Create webhook
        wh = new Webhook(config.getPrefix(), config.getWebHookUrl());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        String serverName = server.name();

        String joinMessage = config.getJoinMessage();
        String message = String.format(joinMessage, event.getEntity().getName().getString());

        if (config.getAppendServerName())
            message += " (" + serverName + ")";

        wh.NotifyWebHook(message, 0);
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event)
    {
        String serverName = server.name();

        String leaveMessage = config.getLeaveMessage();
        String message = String.format(leaveMessage, event.getEntity().getName().getString());

        if (config.getAppendServerName())
            message += " (" + serverName + ")";

        wh.NotifyWebHook(message, 0);
    }

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        if (!config.getPlayerChatNotify())
            return;

        wh.NotifyWebHook("[" + event.getUsername() + "]: " + event.getRawText(), 1);

        String playerMessage = event.getRawText();
        String player =  event.getUsername();
        String format = config.getPlayerChatMessageFormat();

        String generatedMessage = String.format(format, player, playerMessage);
        wh.NotifyWebHook(generatedMessage, 1);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (!config.getPlayerDeathNotify())
            return;

        var entity = event.getEntity();
        
        if (entity instanceof Player)
            wh.NotifyWebHook(event.getSource().getLocalizedDeathMessage(entity).getString(), 2);
    }

    @SubscribeEvent
    public void onPlayerEvent(PlayerEvent event) {
        // LOGGER.info("Das wars!");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
        timer = new Timer(5000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String url = config.getPlayerChatEndpoint() + wh.getPrefix();
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("content-type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // If there is a valid answer with content, post it to the chat
            if (response.statusCode() == 200 && response.body().length() > 0) {
                String name = response.headers().firstValue("X-Name").get();
                if (name == null || name.isEmpty())
                    name = config.getDefaultPlayerChatName();

                // Send a message to all players
                sendBroadCastMessage(server, name  + ": " + response.body());
            }
        }
        catch (Exception ignored)  {
        }
    }

    private void sendBroadCastMessage(MinecraftServer server, String message) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.literal(message));
        }
    }
}