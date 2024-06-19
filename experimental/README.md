This directory used to contain a highly experimental version of SmallWorld that
used [TeaVM](http://teavm.org) to translate the Java bytecode into JavaScript.
This code somewhat worked, but was very incomplete. In the meantime, I have
created an alternative implementation of SmallWorld. In
[smallworld.js](https://github.com/ericscharff/smallworld.js) I rewrote
SmallWorld directly in JavaScript, including a port of the GUI to HTML. There is
also a native HTML UI that allows Smalltalk code to update the DOM of the Web
page on which it is running.
