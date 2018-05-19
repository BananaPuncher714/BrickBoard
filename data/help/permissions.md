***Permissions***
Gives player all permissions available
`brickboard.admin`

Allows player to get all presets installed
`brickboard.inventory.presets`

Allows players to use boards, and/or edit them. Normal users should not be allowed to edit them.

```
brickboard.boards.<board>.access
brickboard.boards.<board>.edit
```

Allows players to recieve or send messages in different channels. Can be used in places such as an announcement channel to stop people from sending messages while being able to read them. The permission to send messages automatically allows the player to read messages.

```
brickboard.channels.<channel>.recieve
brickboard.channels.<channel>.send
```