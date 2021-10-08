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
        // If no argument is specified, assume backtracking is allowed, to meet the spec
        boolean showBacktracking = true;
        if (args.length == 2)
        {
            showBacktracking = Boolean.valueOf(args[1]);
        }
        // Initialise a new Maze and generate from the file provided
        Maze maze;
        try
        {
            maze = new Maze(showBacktracking);
            maze.generateFromFile(args[0]);
            // Solve the maze and output to console
            maze.solveRec();
        }
        catch (StackOverflowError e)
        {
            maze = new Maze(showBacktracking);
            maze.generateFromFile(args[0]);
            // Solve the maze and output to console
            maze.solveItr();
        }
        System.out.println(maze.getSolution());
    }

    private static boolean validateArgs(String[] args)
    {
        String errMess = "Invalid arguments. Usage: <filename:string> [showBacktracking:boolean:default:true]";
        // There should only be one or two arguments
        if (args.length != 1 && args.length != 2)
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

        try
        {
            if (args.length == 2)
            {
                Boolean.valueOf(args[1]);
            }
        }
        catch (Exception e)
        {
            System.err.println(errMess);
            return false;
        }
        return true;
    }
}
