package robotvacuum.house.furniture;

import robotvacuum.collision.CollisionShape;
import java.io.Serializable;

/**
 *
 * @author Tristan Boler
 */
public class Leg implements Serializable {

    private final CollisionShape cShape;

    public Leg(CollisionShape cShape) {
        this.cShape = cShape;
    }

    /**
     * @return the cShape
     */
    public CollisionShape getcShape() {
        return cShape;
    }

}
