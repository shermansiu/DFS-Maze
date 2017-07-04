# DFS Maze

A Java maze generator and solver built on the HSA console.

This maze uses the Depth-First Search algorithm to both generate and solve the maze.

You can control the character (in red) withe the WASD keys to navigate to the endpoint (in teal).

<img src="/img/maze-1.png" alt="DFS Maze" width="623"/>

This project is released under the [CC-BY-SA 3.0 license](https://freedomdefined.org/Licenses/CC-BY-SA-3.0) and the [GNU Free Documentation License](https://en.wikipedia.org/wiki/GNU_Free_Documentation_License).

Scroll down to the Sources heading for more information.

This project was completed on October 30, 2013.

## Installation

### Pre-requisites
This maze uses the HSA library from [Ready to Program](http://compsci.ca/holtsoft/).

You can run `maze.java` straight from the Ready to Program IDE.

### Set-up
You can configure several settings by adjusting some of the static variables near the top of the code.

#### Maze size
To adjust the size of the maze, go to line 9 and adjust this piece of code:

```static int cellSize = __;```

Entering `1` makes the maze ridiculously small and entering `200` makes the maze... unusually easy to solve.
Just don't make the `cellSize` too large, or it'll crash!

#### Solution highlight enable/disable
To prevent the maze from highlighting the solution, go to line 19 and set the value to false as shown here:

```static boolean debugSln = false;```

To re-enable it, set it back to `true`.

## Sources
This maze is adapted from a C implementation of Purpy Pupple's maze generator.

I implemented his maze generator in Java and added the ability to navigate through the maze after its creation.
I also added the ability to see the solution to the maze.

Other minor details include adding a endpoint to the maze, a scoreboard, and debugging features.

https://en.wikipedia.org/wiki/User:Dllu/Maze
