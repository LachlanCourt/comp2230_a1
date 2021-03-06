/*******************************************************************************
 ****    COMP2230 Programming Assignment
 ****    c3308061
 ****    Lachlan Court
 ****    18/09/2021
 ****    This class maintains config for a maze when using the Maze's generate
 ****    from file and solver methods
 *******************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;

public class Config
{
    private ArrayList<Integer> start, end;
    private String vals;
    private int width, height;

    // Constructor
    public Config(int width_, int height_, int start_, int end_, String vals_)
    {
        width = width_;
        height = height_;
        vals = vals_;

        int y = (start_ - 1) / width;
        int x = (start_ - 1) - (y * width);
        start = new ArrayList<Integer>(Arrays.asList(x, y));
        y = (end_ - 1) / width;
        x = (end_ - 1) - (y * width);
        end = new ArrayList<Integer>(Arrays.asList(x, y));
    }

    public ArrayList<Integer> getStart()
    {
        return start;
    }

    public ArrayList<Integer> getEnd()
    {
        return end;
    }

    /**
     * Gets the next value of the vals String and removes it from the String
     * @return The next value of the vals String
     */
    public Cell.WallStates getWallState()
    {
        Cell.WallStates walls = Cell.WallStates.valueOf(Integer.valueOf(vals.substring(0, 1)));
        vals = vals.substring(1);
        return walls;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
