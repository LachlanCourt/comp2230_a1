
# COMP2230 - Algorithms
## Assignment 1
### Task
Assignment was split into two separate problems

1. Create a program that uses a Depth First Search to develop a maze, and output to a text file in a format specified as follows `width,height:start_node:end_node:cell_openness_list`, where `cell_openness_list` describes the state of the right and bottom walls of a cell and hold the values

0: Both closed

1: Right only open

2: Down only open

3: both open

2. Create a program that uses a Depth First Search to solve a maze, given an input file generated from the program described above

### Compile
1. `javac MazeGenerator.java`
2. `javac MazeSolverDFS.java`

### Run
1. `java MazeGenerator <width> <height> <filename>` with `filename` as an output file
2. `java MazeSolverDFS <filename>` where `filename` refers to a file generated from the previous step
