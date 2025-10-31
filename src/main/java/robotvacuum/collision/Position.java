package robotvacuum.collision;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Tristan Boler
 */
public class Position implements Serializable {
    private final double x;
    private final double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position pos) {
        this(pos.getX(), pos.getY());
    }

    /**
     * @param pos the destination position for the angle
     * @return direction from this position to the supplied position in radians from -PI to PI (according to atan2)
     */
    public double directionTo(Position pos) {
        return Math.atan2(this.yDiff(pos), this.xDiff(pos));
    }

    /**
     * @param pos the other position to measure the distance to
     * @return the Pythagorean distance between the positions
     */
    public double distanceTo(Position pos) {
        double xDiff = this.x - pos.getX();
        double yDiff = this.y - pos.getY();
        return Math.hypot(xDiff, yDiff);
    }

    public Position offsetPositionCartesian(Position pos) {
        return new Position(this.x + pos.x, this.y + pos.y);
    }

    public Position offsetPositionPolar(double angle, double distance) {
        return new Position(this.x + Math.cos(angle) * distance, this.y + Math.sin(angle) * distance);
    }

    /**
     * @param start   The start value to interpolate
     * @param end     The end value to interpolate to
     * @param percent The portion between the two to interpolate
     * @return Value that is percent of the way from the start to the end
     */
    private static double linearInterpolate(double start, double end, double percent) {
        return ((end - start) * percent) + start;
    }

    /**
     * @param pos     Position to interpolate to
     * @param percent how far to interpolate to the other position
     * @return The position percent of the way from this position to the supplied position
     */
    public Position linearInterpolateTo(Position pos, double percent) {
        double xPos = linearInterpolate(this.x, pos.getX(), percent);
        double yPos = linearInterpolate(this.y, pos.getY(), percent);
        return new Position(xPos, yPos);
    }

    /**
     * @param pos position to get the difference from
     * @return The difference in x value from pos x to this x
     */
    public double xDiff(Position pos) {
        return pos.getX() - this.x;
    }

    /**
     * @param pos position to get teh difference from
     * @return The difference in y value from pos y to this y
     */
    public double yDiff(Position pos) {
        return pos.getY() - this.y;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean isCloseTo(Position pos) {
        return Math.abs(this.x - pos.x) <= Math.ulp(this.x) && Math.abs(this.y - pos.y) <= Math.ulp(this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;

        return Double.compare(this.x, position.x) == 0 && Double.compare(this.y, position.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
