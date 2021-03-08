This is an extremely hacky experiment to try to run SmallWorld in JavaScript.
It is uses [TeaVM](http://teavm.org) to translate the bytecode into JavaScript.
Surprisingly, it was possible to get the command REPL version of SmallWorld
working in a Web browser with few modifications.

This code is currently extrmely rough, and forks SmallWorld unnecessarily. I
used the standard TeaVM maven archetype to create the fake org.eric package
and entry point in src/main/java/org/eric/Client.java.

If you run

```
mvn package
```

This will create a war file, but you can simply run a Web server from
`target/smallworld-1.0-SNAPSHOT`. The index.html in that directory will run the
SmallWorld text version and the doIt will appear in the javascript console.

The trickiest bit was loading the image, which was hacked around by making an
XMLHttpRequest to fetch the image, and read it using a ByteArrayInputStream.

I hope to clean this up and merge it back into the main repo, if possible.
