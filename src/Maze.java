import java.util.ArrayList;

public class Maze {

    ArrayList<ArrayList<Cell>> maze;

    public Maze()
    {
        maze = new ArrayList<ArrayList<Cell>>();
    }

    public void initFromValues(String[] args)
    {
        int width = Integer.valueOf(args[0]);
        int height = Integer.valueOf(args[1]);
        for (int i = 0; i < height; i++)
        {
            maze.add(new ArrayList<Cell>());
            for (int j = 0; j < width; j++)
            {
                maze.get(i).add(new Cell());
            }
        }
    }

    public void generate()
    {

    }

    @Override
    public String toString()
    {
        return "Maze!";
    }
}
