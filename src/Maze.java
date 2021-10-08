/*******************************************************************************
 ****    COMP2230 Programming Assignment
 ****    c3308061
 ****    Lachlan Court
 ****    18/09/2021
 ****    This class contains the main driver methods to both generate and solve
 ****    a maze using Depth First Search
 *******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Maze
{

    private ArrayList<ArrayList<Cell>> maze;
    private int visitCount;
    private Config config;
    private ArrayList<ArrayList<Integer>> solution;
    private long time;
    private boolean showBacktracking;

    // Constructor
    public Maze()
    {
        maze = new ArrayList<ArrayList<Cell>>();
        visitCount = 0;  // Generator
        time = 0;        // Solver
        showBacktracking = true;
    }

    public Maze(boolean showBacktracking_)
    {
        this();
        showBacktracking = showBacktracking_;
    }

    /**
     * Overloaded initFromValues method which doesn't require an existing config, used to initialise a maze with all
     * four walls on each cell
     * @param width of maze
     * @param height of maze
     */
    private void initFromValues(int width, int height)
    {
        initFromValues(width, height, false);
    }

    /**
     * Initialises the nested ArrayLists that maintains the state of the maze, and applies provided config, if it is
     * available
     * @param width of maze
     * @param height of maze
     * @param hasConfig indicating whether the config member variable is available for use, used when solving an
     *                  existing maze
     */
    private void initFromValues(int width, int height, boolean hasConfig)
    {
        // The structure of the maze is each row ArrayList inside a parent ArrayList, so loop through the height first
        for (int i = 0; i < height; i++)
        {
            // Add a new row
            maze.add(new ArrayList<Cell>());
            for (int j = 0; j < width; j++)
            {
                // Assume that the walls are entirely closed
                Cell.WallStates walls = Cell.WallStates.valueOf(0);
                // Apply config, if it has been initialised
                if (hasConfig)
                {
                    walls = config.getWallState();
                }
                // Add a new Cell to the maze with the initial state of the walls
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

    /**
     * Generates a new maze by selecting a start cell and then using the genDFS method
     * @param width of maze
     * @param height of maze
     */
    public void generateFromValuesRec(int width, int height) throws StackOverflowError
    {
        // Generate blank maze with random start point
        ArrayList<Integer> start = setUpBlankMaze(width, height);
        // Call recursive
        genDFSRecursive(start.get(0), start.get(1));
    }

    public void generateFromValuesItr(int width, int height)
    {
        // Generate blank maze with random start point
        ArrayList<Integer> start = setUpBlankMaze(width, height);
        // Call iterative
        genDFSIterative(start.get(0), start.get(1));
    }

    private ArrayList<Integer> setUpBlankMaze(int width, int height)
    {
        maze = new ArrayList<ArrayList<Cell>>();
        visitCount = 0;
        // Initialise a new maze from scratch
        initFromValues(width, height);

        // Select a starting cell
        Random r = new Random();
        int y = r.nextInt(maze.size());
        int x = r.nextInt(maze.get(y).size());
        maze.get(y).get(x).setStarting(true);
        return new ArrayList<Integer>(Arrays.asList(x, y));
    }

    /**
     * Recursive method to generate a maze
     * @param x the x coordinate of the current cell being looked at
     * @param y the y coordinate of the current cell being looked at
     */
    private void genDFSRecursive(int x, int y)
    {
        // If the cell has not yet been visited, mark it as visited and increment the counter. Method will terminate
        // when the counter is equal to width * height
        if (!maze.get(y).get(x).isVisited())
        {
            maze.get(y).get(x).setVisited(true);
            visitCount++;
        }
        // Get a list of unvisited neighbours to the current cell being looked at
        ArrayList<ArrayList<Integer>> availableNeighbours = getNeighbours(x, y);

        if (availableNeighbours.size() == 0)  // Leaf node
        {
            // If the visit count is equal to width * height then the maze has found its finishing cell
            if (visitCount == maze.size() * maze.get(0).size())
            {
                maze.get(y).get(x).setFinishing(true);
            }
            return;
        }

        // If we have not found the end of the maze, pick new neighbour randomly out of the options
        Random r = new Random();
        int nextIndex = r.nextInt(availableNeighbours.size());
        ArrayList<Integer> next = availableNeighbours.get(nextIndex);
        // Set walls of the surrounding cells assuming that we are about to move to the new neighbour
        setWalls(next, x, y);
        // Call recursive on the new neighbour
        genDFSRecursive(next.get(0), next.get(1));
        // While there are still unvisited neighbours, call recursive on each of those neighbours
        while (getNeighbours(x, y).size() > 0)
        {
            genDFSRecursive(x, y);
        }
    }

    /**
     * Iterative method to generate a maze
     * @param x the x coordinate of the start of the maze
     * @param y the y coordinate of the start of the maze
     */
    private void genDFSIterative(int x, int y)
    {
        ArrayList<ArrayList<Integer>> history = new ArrayList<ArrayList<Integer>>();
        history.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        while (history.size() > 0)
        {
            // If the cell has not yet been visited, mark it as visited and increment the counter. Method will terminate
            // when the counter is equal to width * height
            if (!maze.get(y).get(x).isVisited())
            {
                maze.get(y).get(x).setVisited(true);
                visitCount++;
            }

            // Get a list of unvisited neighbours to the current cell being looked at
            ArrayList<ArrayList<Integer>> availableNeighbours = getNeighbours(x, y);
            while (availableNeighbours.size() == 0)  // Leaf node
            {
                // If the visit count is equal to width * height then the maze has found its finishing cell
                if (visitCount == maze.size() * maze.get(0).size())
                {
                    maze.get(y).get(x).setFinishing(true);
                    return;
                }
                x = history.get(history.size() - 1).get(0);
                y = history.get(history.size() - 1).get(1);
                history.remove(history.size() - 1);
                availableNeighbours = getNeighbours(x, y);
            }

            // If we have not found the end of the maze, pick new neighbour randomly out of the options
            Random r = new Random();
            int nextIndex = r.nextInt(availableNeighbours.size());
            ArrayList<Integer> next = availableNeighbours.get(nextIndex);
            // Set walls of the surrounding cells assuming that we are about to move to the new neighbour
            setWalls(next, x, y);
            x = next.get(0);
            y = next.get(1);
            history.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        }
    }

    private void setWalls(ArrayList<Integer> next, int x, int y)
    {
        // Set walls of the surrounding cell assuming that we are about to move to the new neighbour
        Cell tempCell;
        // If the y coordinate is the one changing
        if (x == next.get(0))
        {
            // If the new cell is to below the current cell
            if (y < next.get(1))
            {
                tempCell = maze.get(y).get(x);
                if (tempCell.getWalls() == Cell.WallStates.BOTH_CLOSED)
                {
                    tempCell.setWalls(Cell.WallStates.BOTTOM_ONLY);
                }
                else if (tempCell.getWalls() == Cell.WallStates.RIGHT_ONLY)
                {
                    tempCell.setWalls(Cell.WallStates.BOTH_OPEN);
                }
            }
            // If the new cell is above the current cell
            else
            {
                tempCell = maze.get(y - 1).get(x);
                if (tempCell.getWalls() == Cell.WallStates.BOTH_CLOSED)
                {
                    tempCell.setWalls(Cell.WallStates.BOTTOM_ONLY);
                }
                else if (tempCell.getWalls() == Cell.WallStates.RIGHT_ONLY)
                {
                    tempCell.setWalls(Cell.WallStates.BOTH_OPEN);
                }
            }
        }
        // If the x coordinate is changing
        else
        {
            // If the new cell is to the right of the current cell
            if (x < next.get(0))
            {
                tempCell = maze.get(y).get(x);
                if (tempCell.getWalls() == Cell.WallStates.BOTH_CLOSED)
                {
                    tempCell.setWalls(Cell.WallStates.RIGHT_ONLY);
                }
                else if (tempCell.getWalls() == Cell.WallStates.BOTTOM_ONLY)
                {
                    tempCell.setWalls(Cell.WallStates.BOTH_OPEN);
                }
            }
            // If the new cell is to the left of the current cell
            else
            {
                tempCell = maze.get(y).get(x - 1);
                if (tempCell.getWalls() == Cell.WallStates.BOTH_CLOSED)
                {
                    tempCell.setWalls(Cell.WallStates.RIGHT_ONLY);
                }
                else if (tempCell.getWalls() == Cell.WallStates.BOTTOM_ONLY)
                {
                    tempCell.setWalls(Cell.WallStates.BOTH_OPEN);
                }
            }
        }
    }

    /**
     * Gets a list of unvisited neighbours to the cell passed in as arguments
     * @param x the x coordinate of the current cell being looked at
     * @param y the y coordinate of the current cell being looked at
     * @return a list of unvisited neighbours
     */
    private ArrayList<ArrayList<Integer>> getNeighbours(int x, int y)
    {
        // Declare a new list to hold any neighbours found
        ArrayList<ArrayList<Integer>> availableNeighbours = new ArrayList<ArrayList<Integer>>();
        Cell tempCell;
        // If we are not on the left wall
        if (x > 0)
        {
            tempCell = maze.get(y).get(x - 1);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x - 1, y)));
            }
        }
        // If we are not on the right wall
        if (x < maze.get(y).size() - 1)
        {
            tempCell = maze.get(y).get(x + 1);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x + 1, y)));
            }
        }
        /// If we are not at the top row
        if (y > 0)
        {
            tempCell = maze.get(y - 1).get(x);
            if (!tempCell.isVisited())
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y - 1)));
            }
        }
        // If we are not on the bottom row
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

    /**
     * Generate a formatted output of the maze as per specification
     * @return a single line representation of the maze
     */
    public String outputDFSToString()
    {
        // Start with the width and height
        String out = maze.get(0).size() + "," + maze.size() + ":";
        // Find start, end, and the walls of each cell
        String end = "";
        String vals = "";
        for (int i = 0; i < maze.size(); i++)
        {
            for (int j = 0; j < maze.get(i).size(); j++)
            {
                // Add the walls of the cell
                vals += maze.get(i).get(j).getWalls().getValue();
                // Add start
                if (maze.get(i).get(j).isStarting())
                {
                    out += (j + (i * maze.get(i).size()) + 1) + ":";
                }
                // Add end
                if (maze.get(i).get(j).isFinishing())
                {
                    end += (j + (i * maze.get(i).size()) + 1) + ":";
                }
            }
        }
        // Concatenate determined data and return
        out += end + vals;
        return out;
    }

    /**
     * Outputs the DFS string to a file
     * @param filename to output the data to
     */
    public void outputDFSToFile(String filename)
    {
        // Attempt to open a print stream according to the specified filename
        PrintWriter out = null;
        try
        {
            out = new PrintWriter(filename);
        }
        catch (FileNotFoundException e)
        {
            // If there is an error opening the PrintWriter
            System.err.println(e);
            System.exit(1);
        }
        // Output to file
        out.println(outputDFSToString());
        out.close();
    }

    /**
     * Generate the maze from a file according to a specified filename
     * @param filename to read the data from
     */
    public void generateFromFile(String filename)
    {
        // Attempt to open a Scanner according to the specified filename
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
        // Try to interpret the data
        try
        {
            // Read the line from the file and split it into an array using a ":" delimeter
            String read = input.nextLine();
            String[] data = read.split(":");
            // Split the first element, the width height, according to a "," delimeter
            String[] widthHeight = data[0].split(",");
            // Assign each element fo the data read array
            int width = Integer.parseInt(widthHeight[0]);
            int height = Integer.parseInt(widthHeight[1]);
            int start = Integer.parseInt(data[1]);
            int end = Integer.parseInt(data[2]);
            String vals = data[3];
            // Create a new config object from the specified data, and initialise the maze with the config
            config = new Config(width, height, start, end, vals);
            initFromValues(width, height, true);
        }
        // If any of the reads fail
        catch (Exception e)
        {
            System.err.print("Invalid file format");
            System.exit(1);
        }
    }

    /**
     * Solves the maze by recursively calling DFS starting at the starting cell
     */
    public void solveRec() throws StackOverflowError
    {
        // Record the time the algorithm starts
        time = java.lang.System.currentTimeMillis();
        // Initialise an arraylist that will contain cell numbers to follow to go from the start to the end of the maze
        solution = new ArrayList<ArrayList<Integer>>();
        // Call recursive
        solveDFSRecursive(config.getStart().get(0), config.getStart().get(1));
        // Calculate the time taken to solve by subtracting the time it started from the current time
        time = java.lang.System.currentTimeMillis() - time;
    }

    public void solveItr()
    {
        // Record the time the algorithm starts
        time = java.lang.System.currentTimeMillis();
        // Initialise an arraylist that will contain cell numbers to follow to go from the start to the end of the maze
        solution = new ArrayList<ArrayList<Integer>>();
        // Call recursive
        solveDFSIterative(config.getStart().get(0), config.getStart().get(1));
        // Calculate the time taken to solve by subtracting the time it started from the current time
        time = java.lang.System.currentTimeMillis() - time;
    }

    /**
     * Recursive method to solve a maze
     * @param x the x coordinate of the current cell being looked at
     * @param y the y coordinate of the current cell being looked at
     */
    private void solveDFSRecursive(int x, int y)
    {
        // If the cell has not yet been visited, mark it as visited and add the cell to the solution
        if (!maze.get(y).get(x).isVisited())
        {
            maze.get(y).get(x).setVisited(true);
            if (!showBacktracking)
            {
                solution.add(new ArrayList<Integer>(Arrays.asList(x, y)));
            }
        }
        if (showBacktracking)
        {
            solution.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        }
        // If the solution has been found there is no need to check neighbours or continue the recursion
        if (isSolved())
        {
            return;
        }

        // Get a list of unvisited neighbours that have open walls to the current cell being looked at
        ArrayList<ArrayList<Integer>> availableNeighbours = getOpenNeighbours(x, y);
        if (availableNeighbours.size() == 0)  // Leaf node
        {
            // This was a dead end, remove it from the solution
            if (!showBacktracking)
            {
                solution.remove(solution.size() - 1);
            }
            return;
        }
        // Always pick the first option so the solution is deterministic
        ArrayList<Integer> next = availableNeighbours.get(0);

        // Call recursive on the new neighbour
        solveDFSRecursive(next.get(0), next.get(1));
        // While there are still unvisited neighbours and the solution has not been found, call recursive on each of
        // those neighbours
        while (getOpenNeighbours(x, y).size() > 0 && !isSolved())
        {
            solveDFSRecursive(x, y);
        }
        // If the solution has not been found and the last element of the solution list is the current cell, we are
        // backtracking after reaching a dead end. Remove it from the list
        if (!showBacktracking)
        {
            if ((!isSolved()) && (x == solution.get(solution.size() - 1).get(0))
                && (y == solution.get(solution.size() - 1).get(1)))
            {
                solution.remove(solution.size() - 1);
            }
        }
    }

    /**
     * Iterative method to solve a maze
     * @param x the x coordinate of the start of the maze
     * @param y the y coordinate of the start of the maze
     */
    private void solveDFSIterative(int x, int y)
    {
        ArrayList<ArrayList<Integer>> history = new ArrayList<ArrayList<Integer>>();
        history.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        while (history.size() > 0)
        {
            // If the cell has not yet been visited, mark it as visited and add the cell to the solution
            if (!maze.get(y).get(x).isVisited())
            {
                maze.get(y).get(x).setVisited(true);
            }
            solution.add(new ArrayList<Integer>(Arrays.asList(x, y)));
            // If the solution has been found there is no need to check neighbours or continue the recursion
            if (isSolved())
            {
                return;
            }
            // Get a list of unvisited neighbours that have open walls to the current cell being looked at
            ArrayList<ArrayList<Integer>> availableNeighbours = getOpenNeighbours(x, y);
            while (availableNeighbours.size() == 0)  // Leaf node
            {
                x = history.get(history.size() - 1).get(0);
                y = history.get(history.size() - 1).get(1);
                history.remove(history.size() - 1);
                availableNeighbours = getOpenNeighbours(x, y);
            }

            // Always pick the first option so the solution is deterministic
            ArrayList<Integer> next = availableNeighbours.get(0);

            x = next.get(0);
            y = next.get(1);
            history.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        }
    }

    /**
     * Checks whether a solution has been found for the maze solver
     * @return whether the solution has been found
     */
    private boolean isSolved()
    {
        // If the x and y coordinates of the end of the solutions list matches the x and y coordinates of the end square
        return solution.get(solution.size() - 1).get(0) == config.getEnd().get(0)
            && solution.get(solution.size() - 1).get(1) == config.getEnd().get(1);
    }

    /**
     * Gets a list of unvisited neighbours with an open wall to the cell passed in as arguments
     * @param x the x coordinate of the current cell being looked at
     * @param y the y coordinate of the current cell being looked at
     * @return a list of unvisited neighbours with an open wall to the current cell
     */
    private ArrayList<ArrayList<Integer>> getOpenNeighbours(int x, int y)
    {
        // Declare a new list to hold any neighbours found
        ArrayList<ArrayList<Integer>> availableNeighbours = new ArrayList<ArrayList<Integer>>();
        Cell tempCell;
        // If we are not on the left wall
        if (x > 0)
        {
            tempCell = maze.get(y).get(x - 1);
            // If the cell is not visited and has an open right wall
            if (!tempCell.isVisited()
                && (tempCell.getWalls() == Cell.WallStates.RIGHT_ONLY
                    || tempCell.getWalls() == Cell.WallStates.BOTH_OPEN))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x - 1, y)));
            }
        }
        // If we are not on the right wall
        if (x < maze.get(y).size() - 1)
        {
            tempCell = maze.get(y).get(x + 1);
            // If the cell is not visited and has an open left wall
            if (!tempCell.isVisited()
                && (maze.get(y).get(x).getWalls() == Cell.WallStates.RIGHT_ONLY
                    || maze.get(y).get(x).getWalls() == Cell.WallStates.BOTH_OPEN))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x + 1, y)));
            }
        }
        // If we are not on the top wall
        if (y > 0)
        {
            tempCell = maze.get(y - 1).get(x);
            // If the cell is not visited and has an open bottom wall
            if (!tempCell.isVisited()
                && (tempCell.getWalls() == Cell.WallStates.BOTTOM_ONLY
                    || tempCell.getWalls() == Cell.WallStates.BOTH_OPEN))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y - 1)));
            }
        }
        // If we are not on the bottom wall
        if (y < maze.size() - 1)
        {
            tempCell = maze.get(y + 1).get(x);
            // If the cell is not visited and has an open top wall
            if (!tempCell.isVisited()
                && (maze.get(y).get(x).getWalls() == Cell.WallStates.BOTTOM_ONLY
                    || maze.get(y).get(x).getWalls() == Cell.WallStates.BOTH_OPEN))
            {
                availableNeighbours.add(new ArrayList<Integer>(Arrays.asList(x, y + 1)));
            }
        }
        return availableNeighbours;
    }

    /**
     * Generate a formatted output of the solution as per specification
     * @return a three line representation of the solution
     */
    public String getSolution()
    {
        String out = "(";
        // Loop through the solution array and calculate the square number from the x and y coordinates
        for (ArrayList<Integer> i : solution)
        {
            // Add 1 as the array indexes from 0 but the output should start at 1
            out += (i.get(1) * config.getWidth() + i.get(0) + 1) + ",";
        }
        out = out.substring(0, out.length() - 1);  // Remove last comma
        out += ")\n";
        out += solution.size() + "\n";
        // Add the time it took to find the solution, calculated in the solve() method
        out += time;
        return out;
    }

    /**
     * Returns a visual representation of the maze using ASCII elements
     * @return an ASCII code representation of the maze
     */
    @Override public String toString()
    {
        // Generate the bar along the top of the maze (Cells only keep track of bottom and right walls so the top left
        // of the maze needs to be hardcoded here when displaying
        String out = "Visual Output:\n "
                     + "_ ".repeat(maze.get(0).size()) + "\n";
        // Loop through the maze and call the overloaded toString() method of each cell
        for (int i = 0; i < maze.size(); i++)
        {
            // Add the bar on the left of the maze
            out += "|";
            for (int j = 0; j < maze.get(i).size(); j++)
            {
                out += maze.get(i).get(j);
            }
            out += "\n";
        }
        return out;
    }
}
