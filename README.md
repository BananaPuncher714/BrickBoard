# BrickBoard - The better GUI by BananaPuncher714
![BrickBoard flicker test](https://i.imgur.com/mOIxt2O.gif "BrickBoard flicker test")
## What is BrickBoard?
BrickBoard is a plugin designed to use the existing chat window of players as a gui. It is an alternative to other forms of menus and boards, such as item guis, and scoreboards.

## What makes this better than other alternatives?
Unlike several other chat menu APIs and libraries, BrickBoard is built with a more modular system. It is incredibly easy to rearrange parts of the board, and just as easy to make custom boxes to go on the board. Besides having a more object oriented approach, BrickBoard also supports custom fonts and can measure the length of messages somewhat accurately. Another feature of BrickBoard is by minimizing the amount of JSON sent to the player by supporting legacy formatting when possible. This prevents the size of the packet from exceeding 32767 bytes, and can let people push the limit farther than before.
### Alternatives include:
- [ChatMenuAPI](https://github.com/timtomtim7/ChatMenuAPI)

## What are some drawbacks?
BrickBoard is meant to take up chat completely, so sending messages directly to the player doesn't work. However, BrickBoard *does* intercept every message sent, and custom modules can be used to make use of the captured messages.

## More documentation:
- [GitBook](https://bananapuncher714.gitbook.io/brickboard/)