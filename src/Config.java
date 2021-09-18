import java.util.ArrayList;
import java.util.Arrays;

public class Config
{
    private ArrayList<Integer> start, end;
    private String vals;
    private int width, height;

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

    public int getWallValue()
    {
        int walls = Integer.valueOf(vals.substring(0, 1));
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
