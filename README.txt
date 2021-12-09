# Project 3: Regular Expression NFA

* Author: Jason Kuphaldt, Connor jackson
* Class: CS361
* Semester: Fall 2021

## Overview

This java application reads a regular expression and 
constructs an NFA based off of the input. 

## Compiling and Using

To compile, execute the following command in the main project directory:
```
$ javac -cp ".:./CS361FA.jar" re/REDriver.java
```

Run the compiled class with the command:
```
$ java -cp ".:./CS361FA.jar" re.REDriver ./tests/[test file name].txt
```


## Discussion
There were several difficult methods in this project. Most notably newNFA and
repetition. There was a very helpful article that was of great use that I will
post in the citations that helped immensely with almost every single method. The
difference being was that the article called for subclasses, where in our code
we had the CS361.jar file instead. 

We had issues getting the program to run because we had built it cooperatively
using VSCode online, and it generated many errors because it wasn't reading
the jar file correctly. This was fixed when we we ran it on onyx. We also
ran into some nullpointer exceptions which were minor issues that were fixed
easily. 


## Sources used

This article was tremendously helpful and you can see that it influenced many of 
our methods:

https://matt.might.net/articles/parsing-regex-with-recursive-descent/