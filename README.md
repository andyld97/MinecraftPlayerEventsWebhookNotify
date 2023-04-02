# Minecraft PlayerEventsWebhookNotify
- A simple webhook for spigot servers (player join, player leave)
- Using HTTP POST

## config.yml
```yaml
appendServerName: false
prefix: '[MC-SRV]'
joinMessage: 'Player %s joined the game!'
leaveMessage: 'Player %s left the game!'
webHookUrl: 'http://your.url'

```

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
