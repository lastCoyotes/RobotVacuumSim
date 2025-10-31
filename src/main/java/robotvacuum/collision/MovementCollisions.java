package robotvacuum.collision;

import java.util.Set;

public class MovementCollisions {
    private final Set<Collision> collisions;
    private final double collisionDistance;
    private final double noCollisionDistance;

    public MovementCollisions(Set<Collision> collisions, double collisionDistance, double noCollisionDistance) {
        this.collisions = collisions;
        this.collisionDistance = collisionDistance;
        this.noCollisionDistance = noCollisionDistance;
    }

    public Set<Collision> getCollisions() {
        return collisions;
    }

    public double getCollisionDistance() {
        return collisionDistance;
    }

    public double getNoCollisionDistance() { return noCollisionDistance; }
}
