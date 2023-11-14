package code.a.software.playerevents;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private String prefix = "[MC-SRV]";

    private String joinMessage = "Player %s joined the game!";

    private String leaveMessage = "Player %s left the game!";

    private String webHookUrl = "http://your.url?param={0}";

    private boolean playerDeathNotify = true;

    private boolean playerChatNotify = false;

    private String playerChatMessage = "[%1$s]: %2$s";

    private String playerChatName = "";

    private String playerChatEndpoint = "";

    private boolean appendServerName = false;

    public String getPrefix() { return prefix; }

    public String getJoinMessage() { return joinMessage; }

    public String getLeaveMessage() { return leaveMessage; }

    public String getWebHookUrl() { return webHookUrl; }

    public boolean getPlayerDeathNotify() { return playerDeathNotify; }

    public boolean getPlayerChatNotify() { return playerChatNotify; }

    public String getPlayerChatMessageFormat() { return playerChatMessage; }

    public String getDefaultPlayerChatName() { return playerChatName; }

    public String getPlayerChatEndpoint() { return playerChatEndpoint; }

    public boolean getAppendServerName() { return appendServerName; }

    // Load Methode
    static Config loadConfig(String path) {
        try {
            var configFile = new File(path);

            if (!configFile.exists()) {
                // Create file if it does not exists
                Gson gson = new Gson();
                var config = new Config();
                Files.write(Paths.get(path), gson.toJson(config).getBytes());

                return config;
            } else {
                String json = Files.readString(Paths.get(path));
                JsonObject jObj = new Gson().fromJson(json, JsonObject.class);

                Config result = new Config();

                result.prefix = getStringWithDefaultValue(jObj, "prefix", result.prefix);
                result.joinMessage = getStringWithDefaultValue(jObj, "joinMessage", result.joinMessage);
                result.leaveMessage = getStringWithDefaultValue(jObj, "leaveMessage", result.leaveMessage);
                result.webHookUrl = getStringWithDefaultValue(jObj, "webHookUrl", result.webHookUrl);
                result.playerDeathNotify = getBoolWithDefaultValue(jObj, "playerDeathNotify", result.playerDeathNotify);
                result.playerChatNotify = getBoolWithDefaultValue(jObj, "playerChatNotify", result.playerChatNotify);
                result.playerChatMessage = getStringWithDefaultValue(jObj, "playerChatMessage", result.playerChatMessage);
                result.playerChatName = getStringWithDefaultValue(jObj, "playerChatName", result.playerChatName);
                result.playerChatEndpoint = getStringWithDefaultValue(jObj, "playerChatEndpoint", result.playerChatEndpoint);
                result.appendServerName = getBoolWithDefaultValue(jObj, "appendServerName", result.appendServerName);

                return result;
            }
        }
        catch (Exception ex)
        {
            return new Config();
        }
    }

    private static String getStringWithDefaultValue(JsonObject jObj, String key, String defaultValue)
    {
        String result = defaultValue;

        JsonElement element = jObj.get(key);
        if (element != null)
        {
            String tmp = element.getAsString();
            if (tmp != null && !tmp.isEmpty())
                result = tmp;
        }

        return result;
    }

    private static boolean getBoolWithDefaultValue(JsonObject jObj, String key, boolean defaultValue)
    {
        boolean result = defaultValue;

        JsonElement element = jObj.get(key);
        if (element != null)
            result = element.getAsBoolean();

        return result;
    }
}
