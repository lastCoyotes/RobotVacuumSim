package robotvacuum.house.furniture;

import robotvacuum.collision.CollisionShape;
import robotvacuum.collision.CollisionTestData;
import robotvacuum.collision.Position;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Tristan Boler  
 */

public class Chair implements Furniture, Serializable {

    private final Map<Position, Leg> legs;

    public Chair(Map<Position, Leg> legs) {
        this.legs = new HashMap<>(legs);
    }

    /**
     * @return the legs
     */
    public Map<Position, Leg> getLegs() {
        return legs;
    }

    @Override
    public Collection<CollisionTestData<? extends CollisionShape>> getCollisionTestData() {
        return legs.entrySet().stream().map(e -> new CollisionTestData<>(e.getKey(), e.getValue().getcShape())).collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.legs);
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
        final Chair other = (Chair) obj;
        return Objects.equals(this.legs, other.legs);
    }
}
