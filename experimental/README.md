This directory used to host a hacky experiment which used TeaVM to translate the
Java SmallWorld interpreter into JavaScript. While this did basic things, the
generated code was not meant for modification.

Instead, I've been playing with a hand-written reimplementation of SmallWorld,
called [smallworld.js](https://github.com/ericscharff/smallworld.js). This has
a GUI and a full set of SmallWorld bytecode and primitive support. It also has
a REPL. Users are encouraged to check it out!
