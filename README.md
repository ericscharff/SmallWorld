# SmallWorld

SmallWorld is a very simple Smalltalk Virtual Machine written in Java. It is
derived from Tim Budd's Smalltalk. The original import was taken from
the state of the SmallWorld.tar file found at
This is the initial import of Tim Budd's SmallWorld, a very simple Smalltalk
Virtual Machine written in Java. This is the state of the SmallWorld.tar
file found at
http://web.engr.oregonstate.edu/~budd/SmallWorld/Source/SmallWorld.tar
dated 10-Nov-2004

This version extends the original in several ways:

* The serialization format no longer depends on Java serialization.
* The GUI has been decoupled from the interpreter.
* The Smalltalk classes are in plain text files for easier browsing.
* Code has been reformatted, and warnings/deprecation reduced.

# Running SmallWorld

SmallWorld is built and run with Gradle.  Simply run

```
gradle run
```

and you're good to go! It will open a Swing-based user interface for
evaluating expressions, browsing classes, and so on.
