# SmallWorld

SmallWorld is a very simple Smalltalk Virtual Machine written in Java. It is
derived from Tim Budd's Smalltalk. The original import was taken from the state
of the SmallWorld.tar file found at
http://web.engr.oregonstate.edu/~budd/SmallWorld/Source/SmallWorld.tar dated
10-Nov-2004

This version extends the original in several ways:

*   The serialization format no longer depends on Java serialization.
*   The GUI has been decoupled from the interpreter.
*   The Smalltalk classes are in plain text files for easier browsing. These
    were extracted from the main image using `Class>>fileOut`. Also included
    are some classes (related to the compiler) which are in the image, but not
    listed in the main class browser (they were probably explicitly removed.)
*   Code has been reformatted, and warnings/deprecation reduced.

# Running SmallWorld

SmallWorld is built and run with Gradle. Simply run

```
./gradlew run
```

and you're good to go! It will open a Swing-based user interface for evaluating
expressions, browsing classes, and so on.

# SmallWorld from the command line

It is possible to run a version of SmallWorld from the command line. Look at
`build.gradle` for more details. You should then be able to run `./gradew
commandline` to start tyhe interpreter. Typing commands at this prompt will
cause the input string to be run ("printIt"). For example:

```
> Task :commandline
image initialized
SmallWorld> 3 + 2
Running task: 3 + 2
SmallInt: 5
Task complete
SmallWorld> 'hello' size
Running task: 'hello' size
SmallInt: 5
Task complete
SmallWorld> ((1 / 3) + (3 / 4)) printString
Running task: ((1 / 3) + (3 / 4)) printString
(13/12)
Task complete
SmallWorld>
```

Hit `Ctrl-D` to exit.
