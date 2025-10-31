package robotvacuum.collision;

import java.io.Serializable;

/**
 * @author Tristan Boler
 */
public class CollisionTestData<S extends CollisionShape> implements Serializable {
    private final Position pos;
    private final S cShape;

    public CollisionTestData(Position pos, S cShape) {
        this.pos = pos;
        this.cShape = cShape;
    }

    /**
     * @return the pos
     */
    public Position getPos() {
        return pos;
    }

    /**
     * @return the cShape
     */
    public S getcShape() {
        return cShape;
    }

}
