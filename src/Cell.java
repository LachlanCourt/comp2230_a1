/*******************************************************************************
 ****    COMP2230 Programming Assignment
 ****    c3308061
 ****    Lachlan Court
 ****    18/09/2021
 ****    This class represents a cell in the maze. It knows whether it is the
 ****    starting or finishing cell, as well as what walls it has, and keeps
 ****    track of whether it has been "visited", a concept used for DFS
 *******************************************************************************/

public class Cell
{
    private int walls;
    private boolean visited;
    private boolean starting;
    private boolean finishing;

    // Constructor
    public Cell(int walls_)
    {
        walls = walls_;
        visited = false;
        starting = false;
        finishing = false;
    }

    // Getters and setters

    public int getWalls()
    {
        return walls;
    }

    public void setWalls(int walls_)
    {
        this.walls = walls_;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited_)
    {
        this.visited = visited_;
    }

    public boolean isStarting()
    {
        return starting;
    }

    public void setStarting(boolean starting_)
    {
        this.starting = starting_;
    }

    public boolean isFinishing()
    {
        return finishing;
    }

    public void setFinishing(boolean finishing_)
    {
        this.finishing = finishing_;
    }

    /**
     * Converts the integer number of walls to a series of ASCII symbols to draw the map
     * @return an ASCII pair indicating the state of that square
     */
    @Override public String toString()
    {
        switch (walls)
        {
            case 0:
                return "_|";
            case 1:
                return "_ ";
            case 2:
                return " |";
            case 3:
                return "  ";
            default:
                return "";
        }
    }
}
