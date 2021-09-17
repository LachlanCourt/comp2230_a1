public class Cell
{
    int walls;
    boolean visited;
    boolean starting;
    boolean finishing;

    public Cell()
    {
        visited = false;
        starting = false;
        finishing = false;
    }

    public int getWalls() {
        return walls;
    }

    public void setWalls(int walls_) {
        this.walls = walls_;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited_) {
        this.visited = visited_;
    }

    public boolean isStarting() {
        return starting;
    }

    public void setStarting(boolean starting_) {
        this.starting = starting_;
    }

    public boolean isFinishing() {
        return finishing;
    }

    public void setFinishing(boolean finishing_) {
        this.finishing = finishing_;
    }

    @Override
    public String toString()
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
