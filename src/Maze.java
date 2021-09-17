import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze
{

    private ArrayList<ArrayList<Cell>> maze;
    private int visitCount;

    public Maze()
    {
        maze = new ArrayList<ArrayList<Cell>>();
        visitCount = 0;
    }

    public void initFromValues(int width, int height)
    {
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
        Random r = new Random();
        int y = r.nextInt(maze.size());
        int x = r.nextInt(maze.get(y).size());

        // System.out.println(x +" "+ y);
        maze.get(y).get(x).setStarting(true);
        genDFS(x, y);
    }

    private void genDFS(int x, int y)
    {
        if (!maze.get(y).get(x).isVisited())
        {
            maze.get(y).get(x).setVisited(true);
            visitCount++;
        }
        System.out.println("x " + x + ".....y " + y + "..... " + visitCount);
        ArrayList<ArrayList<Integer>> availableNeighbours = getNeighbours(x, y);

        if (availableNeighbours.size() == 0)  // Leaf node
        {
            if (visitCount >= maze.size() * maze.get(0).size())
            {
                maze.get(y).get(x).setFinishing(true);
            }
            // System.out.println("LEAF: " + x + " " + y);
            return;
        }
        // Pick new neighbour
        Random r = new Random();
        int nextIndex = r.nextInt(availableNeighbours.size());
        ArrayList<Integer> next = availableNeighbours.get(nextIndex);

        // Set walls
        Cell tempCell;
        if (x == next.get(0))
        {
            if (y < next.get(1))
            {
                tempCell = maze.get(y).get(x);
                if (tempCell.getWalls() == 0)
                {
                    tempCell.setWalls(2);
                }
                else if (tempCell.getWalls() == 1)
                {
                    tempCell.setWalls(3);
                }
            }
            else
            {
                tempCell = maze.get(y - 1).get(x);
                if (tempCell.getWalls() == 0)
                {
                    tempCell.setWalls(2);
                }
                else if (tempCell.getWalls() == 1)
                {
                    tempCell.setWalls(3);
                }
            }
        }
        else
        {
            if (x < next.get(0))
            {
                tempCell = maze.get(y).get(x);
                if (tempCell.getWalls() == 0)
                {
                    tempCell.setWalls(1);
                }
                else if (tempCell.getWalls() == 2)
                {
                    tempCell.setWalls(3);
                }
            }
            else
            {
                tempCell = maze.get(y).get(x - 1);
                if (tempCell.getWalls() == 0)
                {
                    tempCell.setWalls(1);
                }
                else if (tempCell.getWalls() == 2)
                {
                    tempCell.setWalls(3);
                }
            }
        }
        // System.out.println(this);
        genDFS(next.get(0), next.get(1));
        while (getNeighbours(x, y).size() > 0)
        {
            genDFS(x, y);
        }
    }

    private ArrayList<ArrayList<Integer>> getNeighbours(int x, int y)
    {
        ArrayList<ArrayList<Integer>> availableNeighbours = new ArrayList<ArrayList<Integer>>();
        Cell tempCell;
        if (x > 0)
        {
            tempCell = maze.get(y).get(x - 1);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x - 1, y)));
            }
        }
        if (x < maze.get(y).size() - 1)
        {
            tempCell = maze.get(y).get(x + 1);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x + 1, y)));
            }
        }
        if (y > 0)
        {
            tempCell = maze.get(y - 1).get(x);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y - 1)));
            }
        }
        if (y < maze.size() - 1)
        {
            tempCell = maze.get(y + 1).get(x);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y + 1)));
            }
        }
        return availableNeighbours;
    }

    @Override public String toString()
    {
        String out = "Visual Output:\n "
                     + "_ ".repeat(maze.get(0).size()) + "\n";
        for (int i = 0; i < maze.size(); i++)
        {
            out += "|";
            for (int j = 0; j < maze.get(i).size(); j++)
            {
                out += maze.get(i).get(j);
            }
            out += "\n";
        }

        out += "File output:\n";
        out += maze.get(0).size() + "," + maze.size() + ":";
        // Find start
        String end = "";
        String vals = "";
        for (int i = 0; i < maze.size(); i++)
        {
            for (int j = 0; j < maze.get(i).size(); j++)
            {
                vals += maze.get(i).get(j).getWalls();
                if (maze.get(i).get(j).isStarting())
                {
                    out += (j + (i * maze.get(i).size()) + 1) + ":";
                }
                if (maze.get(i).get(j).isFinishing())
                {
                    end += (j + (i * maze.get(i).size()) + 1) + ":";
                }
            }
        }
        out += end + vals;
        return out;
    }

    public String toDFS()
    {
        return "";
    }
}
