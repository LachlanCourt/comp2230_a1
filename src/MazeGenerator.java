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
        Maze maze = new Maze();
        maze.initFromValues(args);
        maze.generate();


        System.out.println(maze);
    }

    public static boolean validateArgs(String[] args)
    {
        String errMess = "Invalid arguments. Usage: <width:int> <height:int> <filename:string>";
        if (args.length != 3)
        {
            System.err.println(errMess);
            return false;
        }
        try
        {
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
