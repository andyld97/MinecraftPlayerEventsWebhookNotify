# Minecraft PlayerEventsWebhookNotify
- A simple webhook for spigot servers (player join, player leave)
- Using HTTP POST

## config.yml
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

Note that `playerChatName` and `playerChatEndpoint` are currently only for internal usage (they are just not yet documented)

## Response you will recieve
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
