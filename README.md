# Minecraft PlayerEventsWebhookNotify
- A simple webhook for spigot servers (player join, player leave)
- Using HTTP GET Param (Url Encoded)
- TODO: POST Impl

## config.yml
```yaml
appendServerName: false
prefix: '[MC-SRV]'
joinMessage: 'Player %s joined the game!'
leaveMessage: 'Player %s left the game!'
webHookUrl: 'http://your.url?param={0}'

```
