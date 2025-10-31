package robotvacuum.collision;

import java.io.Serializable;

public abstract class CollisionShape implements Serializable {

    private final Shape shape;

    protected CollisionShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * @return the shape
     */
    public Shape getShape() {
        return shape;
    }

    abstract public double getArea();
}
