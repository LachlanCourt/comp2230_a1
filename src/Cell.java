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
    public enum WallStates {
        BOTH_CLOSED(0),
        RIGHT_ONLY(1),
        BOTTOM_ONLY(2),
        BOTH_OPEN(3);
        private int value;
        WallStates(int value_)
        {
            this.value = value_;
        }
        public static WallStates valueOf(Integer value_)
        {
            switch (value_)
            {
                case 0:
                    return BOTH_CLOSED;
                case 1:
                    return RIGHT_ONLY;
                case 2:
                    return BOTTOM_ONLY;
                case 3:
                    return BOTH_OPEN;
                default:
                    return BOTH_CLOSED;
            }
        }
        public int getValue()
        {
            return this.value;
        }
    }

    private WallStates walls;
    private boolean visited;
    private boolean starting;
    private boolean finishing;


    // Constructor
    public Cell(WallStates walls_)
    {
        walls = walls_;
        visited = false;
        starting = false;
        finishing = false;
    }

    // Getters and setters

    public WallStates getWalls()
    {
        return walls;
    }

    public void setWalls(WallStates walls_)
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
            case BOTH_CLOSED:
                return "_|";
            case RIGHT_ONLY:
                return "_ ";
            case BOTTOM_ONLY:
                return " |";
            case BOTH_OPEN:
                return "  ";
            default:
                return "";
        }
    }
}
