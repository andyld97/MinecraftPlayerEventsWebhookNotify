# Minecraft PlayerEventsWebhookNotify (Spigot & Forge)
- A simple webhook for spigot servers (player join, player leave)
- Using HTTP POST

# Spigot

- Copy PlayerEventsWebhookNotifySpigot.jar into the plugin folder of your server.
- After the first start you will get your `config.yml`-file in `plugins/PlayerEventsWebhookNotify/config.yaml`!

## `config.yml`
```yaml
appendServerName: false
prefix: '[MC-SRV]'
joinMessage: 'Player %s joined the game!'
leaveMessage: 'Player %s left the game!'
playerDeathNotify: true
playerChatNotify: false
playerChatMessage: '[%1$s]: %2$s'
playerChatName: 'YOUR_NAME'
playerChatEndpoint: 'PLAYER_CHAT_ANSWER_EP'
webHookUrl: 'http://your.url'
```

# Forge

- Copy PlayerEventsWebhookNotifyForge.jar into the mod-folder of your server.
- Create the file `config.json` in `config/PlayerWebhookNotify.json`!

## `PlayerWebhookNotify.json`
```json
{
  "appendServerName":false,
  "prefix":"[MC-SRV]",
  "joinMessage":"Player %s joined the game!",
  "leaveMessage":"Player %s left the game!",
  "webHookUrl":"http://your.url",
  "playerDeathNotify":true,
  "playerChatNotify":true,
  "playerChatMessage":"[%1$s]: %2$s",
  "playerChatName":"YOUR_NAME",
  "playerChatEndpoint":"PLAYER_CHAT_ANSWER_EP"
}
```

# General 
Note that `playerChatName` and `playerChatEndpoint` are currently only for internal usage (they are just not yet documented)

## Response you will receive
```json
{
   "level": 0,
   "timestamp": "ISO 8601 confirm timestamp",
   "application": "Minecraft",
   "message": "a message",
   "scope": "your-prefix",
   "device": "your-device",
   "userAgent": "Spigot/PluginV1.0.0"   
}
```
### Log Level
- 0: Info (e.g. Player Join)
- 1: Player Chat
- 2: Death Messages
