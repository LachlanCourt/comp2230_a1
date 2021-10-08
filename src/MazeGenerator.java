/*******************************************************************************
 ****    COMP2230 Programming Assignment
 ****    c3308061
 ****    Lachlan Court
 ****    18/09/2021
 ****    This class is the main class of a program that generates a maze using
 ****    a Depth First Search algorithm. It validates the command line arguments
 ****    and then calls the Maze class's driver methods
 *******************************************************************************/

public class MazeGenerator
{
    public static void main(String[] args)
    {
        // If arguments are invalid, the program cannot continue
        if (!validateArgs(args))
        {
            System.exit(1);
        }
        MazeGenerator MG = new MazeGenerator();
        MG.run(args);
    }

    public void run(String[] args)
    {
        // Initialise a new Maze and generate from command line arguments
        Maze maze = new Maze();
        try
        {
            maze.generateFromValuesRec(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        }
        catch (StackOverflowError e)
        {
            maze.generateFromValuesItr(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        }
        // Output the maze to console, and then write to file
        System.out.println(maze);
        maze.outputDFSToFile(args[2]);
        System.out.println("Data outputted to text file " + args[2] + "!");
    }

    public static boolean validateArgs(String[] args)
    {
        String errMess =
            "Invalid arguments. Usage: <width:int> <height:int> <filename:string> where width and height are greater than 0";
        // There should be three arguments
        if (args.length != 3)
        {
            System.err.println(errMess);
            return false;
        }
        try
        {
            // Ensure the first two arguments are integers
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);

            if (x < 1 || y < 1)
            {
                System.err.println(errMess);
                return false;
            }
        }
        catch (Exception e)
        {
            System.err.println(errMess);
            return false;
        }
        // The third argument is a filename to write to. No validation is required
        return true;
    }
}
