package robotvacuum.house.furniture;

import java.util.Collection;
import java.util.Set;

import robotvacuum.collision.CollisionRectangle;
import robotvacuum.collision.CollisionShape;
import robotvacuum.collision.CollisionTestData;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Tristan Boler  
 */

public class Chest implements Furniture, Serializable {

    private final CollisionTestData<CollisionRectangle> cData;

    public Chest(CollisionTestData<CollisionRectangle> cData) {
        this.cData = cData;
    }

    @Override
    public Collection<CollisionTestData<? extends CollisionShape>> getCollisionTestData() {
        return Set.of(cData);
    }

    /**
     * Get the value of cData
     *
     * @return the value of cData
     */
    public CollisionTestData<CollisionRectangle> getcData() {
        return cData;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.cData);
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
        final Chest other = (Chest) obj;
        return Objects.equals(this.cData, other.cData);
    }
}
