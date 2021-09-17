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
        ArrayList<ArrayList<Cell>> grid = generateGrid(args);
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

    public ArrayList<ArrayList<Cell>> generateGrid(String[] args)
    {
        ArrayList<ArrayList<Cell>> grid = new ArrayList<ArrayList<Cell>>();
        int width = Integer.valueOf(args[0]);
        int height = Integer.valueOf(args[1]);
        for (int i = 0; i < height; i++)
        {
            grid.add(new ArrayList<Cell>());
            for (int j = 0; j < width; j++)
            {
                grid.get(i).add(new Cell());
            }
        }
        return grid;
    }
}
