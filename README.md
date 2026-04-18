# SwingX - Library

This is a fork of the great SwingX library. Its a pity it was not developed any further. 

## Modifications version 2.0.0

* minimum JDK 17
* upgraded plugins
* upgrade Mockito to version 5
* use of JApplet removed
* spotless / palantir source code formatting 
* introduced more modern language constructs (instanceof pattern, ..)
* added missing generics types 
* upgraded to JUnit 5
* allow build up to JDK 26 (however, demo does not run here)
* replace Hamcrest with assertj

## Modifications version 1.6.6

* WebStart was completely removed
* OpenJDK 8 buildable
* SwingX - Demos integrated in main build
* SwingX - Demos startable using main class

------------

# Readme - SwingLabs SwingX Project - http://swingx.dev.java.net


SwingX is a library of components and utilities extending the Java Swing library; read more at our website, 
http://swingx.dev.java.net, and Wiki page, http://wiki.java.net/bin/view/Javadesktop/SwingLabsSwingX


## Getting the Latest Source

1) Check out the lastest code
Download the latest release from our SVN repository; full instructions are at
https://swingx.dev.java.net/servlets/ProjectSource

## Building the Source

SwingX relies on Maven for controlling compilation, building docs, testing, etc. You can use our POM files to build the project, some IDEs can directly invoke Maven for you.

To compile from the command line, you'll need to have Apache Maven 3.x installed; see http://maven.apache.org. 

You can build SwingX by going to the command line and typing
mvn package

That should be it--this will test and build swingx.jar in the target directory. 
