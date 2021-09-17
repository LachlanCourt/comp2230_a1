import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Maze
{

    private ArrayList<ArrayList<Cell>> maze;
    private int visitCount;
    private Config config;
    private ArrayList<ArrayList<Integer>> solution;
    private long time;

    public Maze()
    {
        maze = new ArrayList<ArrayList<Cell>>();
        visitCount = 0;
        time = 0;
    }

    public void initFromValues(int width, int height)
    {
        initFromValues(width, height, false);
    }

    public void initFromValues(int width, int height, boolean hasConfig)
    {
        for (int i = 0; i < height; i++)
        {
            maze.add(new ArrayList<Cell>());
            for (int j = 0; j < width; j++)
            {
                int walls = 0;
                if (hasConfig)
                {
                    walls = config.getValue();
                }
                maze.get(i).add(new Cell(walls));
            }
        }

        // Set start and end
        if (hasConfig)
        {
            maze.get(config.getStart().get(1)).get(config.getStart().get(0)).setStarting(true);
            maze.get(config.getEnd().get(1)).get(config.getEnd().get(0)).setFinishing(true);
        }
    }

    public void generate()
    {
        Random r = new Random();
        int y = r.nextInt(maze.size());
        int x = r.nextInt(maze.get(y).size());

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
        ArrayList<ArrayList<Integer>> availableNeighbours = getNeighbours(x, y);

        if (availableNeighbours.size() == 0)  // Leaf node
        {
            if (visitCount >= maze.size() * maze.get(0).size())
            {
                maze.get(y).get(x).setFinishing(true);
            }
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
        return out;
    }

    public String toDFS()
    {
        String out = maze.get(0).size() + "," + maze.size() + ":";
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

    public void outputToFile(String filename)
    {
        // Output to text file
        PrintWriter out;
        try
        {
            // Declare a new PrintWriter
            out = new PrintWriter(filename);
        }
        catch (FileNotFoundException e)
        {
            // If there is an error opening the PrintWriter
            System.err.println(e);
            return;
        }
        // Output to file
        out.println(toDFS());
        out.close();
    }

    public void generateFromFile(String filename)
    {
        Scanner input = null;
        try
        {
            input = new Scanner(new File(filename));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("File does not exist");
            System.exit(1);
        }
        try
        {
            String read = input.nextLine();
            String[] data = read.split(":");
            String[] widthHeight = data[0].split(",");
            int width = Integer.parseInt(widthHeight[0]);
            int height = Integer.parseInt(widthHeight[1]);
            int start = Integer.parseInt(data[1]);
            int end = Integer.parseInt(data[2]);
            String vals = data[3];
            config = new Config(width, height, start, end, vals);
            initFromValues(width, height, true);
        }
        catch (Exception e)
        {
            System.err.print("Invalid file format");
            System.exit(1);
        }
    }

    public void solve()
    {
        time = java.lang.System.currentTimeMillis();
        solution = new ArrayList<ArrayList<Integer>>();
        solveDFS(config.getStart().get(0), config.getStart().get(1));
        time = java.lang.System.currentTimeMillis() - time;
    }

    private void solveDFS(int x, int y)
    {
        if (!maze.get(y).get(x).isVisited())
        {
            maze.get(y).get(x).setVisited(true);
            solution.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        }
        if (isSolved())
        {
            return;
        }

        ArrayList<ArrayList<Integer>> availableNeighbours = getOpenNeighbours(x, y);
        if (availableNeighbours.size() == 0)  // Leaf node
        {
            solution.remove(solution.size() - 1);
            return;
        }
        ArrayList<Integer> next = availableNeighbours.get(0);

        solveDFS(next.get(0), next.get(1));
        while (getOpenNeighbours(x, y).size() > 0 && !isSolved())
        {
            solveDFS(x, y);
        }
        if ((!isSolved()) && (x == solution.get(solution.size() - 1).get(0))
            && (y == solution.get(solution.size() - 1).get(1)))
        {
            ArrayList<Integer> a = solution.get(solution.size() - 1);
            solution.remove(solution.size() - 1);
        }
    }

    private boolean isSolved()
    {
        return solution.get(solution.size() - 1).get(0) == config.getEnd().get(0)
            && solution.get(solution.size() - 1).get(1) == config.getEnd().get(1);
    }

    private ArrayList<ArrayList<Integer>> getOpenNeighbours(int x, int y)
    {
        ArrayList<ArrayList<Integer>> availableNeighbours = new ArrayList<ArrayList<Integer>>();
        Cell tempCell;
        if (x > 0)
        {
            tempCell = maze.get(y).get(x - 1);
            if (!tempCell.isVisited() && (tempCell.getWalls() == 1 || tempCell.getWalls() == 3))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x - 1, y)));
            }
        }
        if (x < maze.get(y).size() - 1)
        {
            tempCell = maze.get(y).get(x + 1);
            if (!tempCell.isVisited() && (maze.get(y).get(x).getWalls() == 1 || maze.get(y).get(x).getWalls() == 3))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x + 1, y)));
            }
        }
        if (y > 0)
        {
            tempCell = maze.get(y - 1).get(x);
            if (!tempCell.isVisited() && (tempCell.getWalls() == 2 || tempCell.getWalls() == 3))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y - 1)));
            }
        }
        if (y < maze.size() - 1)
        {
            tempCell = maze.get(y + 1).get(x);
            if (!tempCell.isVisited() && (maze.get(y).get(x).getWalls() == 2 || maze.get(y).get(x).getWalls() == 3))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y + 1)));
            }
        }
        return availableNeighbours;
    }

    public String getSolved()
    {
        String out = "(";
        for (ArrayList<Integer> i : solution)
        {
            out += (i.get(1) * config.getWidth() + i.get(0) + 1) + ",";
        }
        out = out.substring(0, out.length() - 1);  // Remove last comma
        out += ")\n";
        out += solution.size() + "\n";
        out += time;
        return out;
    }
}
