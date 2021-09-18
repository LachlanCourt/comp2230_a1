import java.sql.Array;
import java.util.ArrayList;

public class MazeGenerator
{
    public static void main(String[] args)
    {
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
        maze.generateFromValues(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        // Output the maze to console, and then write to file
        System.out.println(maze);
        maze.outputDFSToFile(args[2]);
        System.out.println("Data outputted to text file " + args[2] + "!");
    }

    public static boolean validateArgs(String[] args)
    {
        String errMess = "Invalid arguments. Usage: <width:int> <height:int> <filename:string>";
        // There should be three arguments
        if (args.length != 3)
        {
            System.err.println(errMess);
            return false;
        }
        try
        {
            // Ensure the first two arguments are integers
            Integer.parseInt(args[0]);
            Integer.parseInt(args[1]);
        }
        catch (Exception e)
        {
            System.err.println(errMess);
            return false;
        }
        return true;
    }
}
