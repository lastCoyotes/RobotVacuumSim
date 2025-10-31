package robotvacuum.collision;

/**
 * @author Tristan Boler
 */
public class CollisionRectangle extends CollisionShape {

    private final double width;
    private final double height;

    public CollisionRectangle(double width, double height) {
        super(Shape.RECTANGLE);
        this.width = width;
        this.height = height;
    }

    public CollisionRectangle(CollisionRectangle cRect) {
        this(cRect.getWidth(), cRect.getHeight());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CollisionRectangle other = (CollisionRectangle) obj;
        return Double.doubleToLongBits(this.width) == Double.doubleToLongBits(other.width)
                && Double.doubleToLongBits(this.height) == Double.doubleToLongBits(other.height);
    }


    /**
     * Get the value of width
     *
     * @return the value of width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the value of height
     *
     * @return the value of height
     */
    public double getHeight() {
        return height;
    }

    @Override
    public double getArea() {
        return this.height * this.width;
    }
}
