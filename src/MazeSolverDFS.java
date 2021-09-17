import java.io.File;

public class MazeSolverDFS
{
    public static void main(String[] args)
    {
        if (!validateArgs(args))
        {
            System.exit(1);
        }
        MazeSolverDFS MS = new MazeSolverDFS();
        MS.run(args);
    }

    public void run(String[] args)
    {
        Maze maze = new Maze();
        maze.generateFromFile(args[0]);
        maze.solve();
        // System.out.println(maze);
        maze.outputSolved();
        // System.out.println("Data outputted to text file!");
    }

    private static boolean validateArgs(String[] args)
    {
        String errMess = "Invalid arguments. Usage: <filename:string>";
        if (args.length != 1)
        {
            System.err.println(errMess);
            return false;
        }
        File f = new File(args[0]);
        if (!f.exists())
        {
            System.err.println(errMess);
            return false;
        }
        return true;
    }
}
