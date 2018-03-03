/**
 * Class containing a constructor and fields for a board position
 */
public class Position {

    //Private integers representing x and y positions on the board
    private int x;
    private int y;

    //Constructor to simply set a board position
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Getters and setters for x and y positions
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //Overriding the hashCode method to return a coordinate-specific value rather than object-specific
    @Override
    public int hashCode() {
        return x + 10 * y;
    }

    //Method to check if an object is in the specified position
    @Override
    public boolean equals(Object obj) {
        if (this.x == ((Position)obj).getX() && this.y == ((Position)obj).getY()) {
            return true;
        }
        return false;
    }
}
