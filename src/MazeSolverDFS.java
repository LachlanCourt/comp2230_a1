/*******************************************************************************
 ****    COMP2230 Programming Assignment
 ****    c3308061
 ****    Lachlan Court
 ****    18/09/2021
 ****    This class is the main class of a program that solves a maze using a
 ****    Depth First Search algorithm. It validates the command line arguments
 ****    and then calls the Maze class's driver methods
 *******************************************************************************/

import java.io.File;

public class MazeSolverDFS
{
    public static void main(String[] args)
    {
        // If arguments are invalid, the program cannot continue
        if (!validateArgs(args))
        {
            System.exit(1);
        }
        MazeSolverDFS MS = new MazeSolverDFS();
        MS.run(args);
    }

    public void run(String[] args)
    {
        // Initialise a new Maze and generate from the file provided
        Maze maze = new Maze();
        maze.generateFromFile(args[0]);
        // Solve the maze and output to console
        maze.solve();
        System.out.println(maze.getSolution());
    }

    private static boolean validateArgs(String[] args)
    {
        String errMess = "Invalid arguments. Usage: <filename:string>";
        // There should only be a single argument
        if (args.length != 1)
        {
            System.err.println(errMess);
            return false;
        }
        // Check that a file exists with the given filename
        File f = new File(args[0]);
        if (!f.exists())
        {
            System.err.println(errMess);
            return false;
        }
        return true;
    }
}
