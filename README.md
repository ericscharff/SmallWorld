# SmallWorld

SmallWorld is a very simple Smalltalk Virtual Machine written in Java. It is
derived from Tim Budd's Smalltalk. The original import in this repository was
taken from the state of the SmallWorld.tar file found at
http://web.engr.oregonstate.edu/~budd/SmallWorld/Source/SmallWorld.tar dated
10-Nov-2004

This version extends the original in several ways:

*   The serialization format no longer depends on Java serialization.
*   The GUI has been decoupled from the interpreter.
*   The Smalltalk classes are in plain text files for easier browsing. These
    were extracted from the main image using `Class>>fileOut`. Also included are
    some classes (related to the compiler) which are in the image, but not
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
SmallWorld> 3 + 4
Running task: 3 + 4
SmallInt: 7
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

# Experimental features

In the `experimental` directory, there is a proof-of-concept of transpiling
SmallWorld into JavaScript. Very simple expressions can be evaluated and printed
within a Web page.
The MIT License (MIT)

Copyright (c) 2004-2022 Tim Budd

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
