package robotvacuum.robot;

import robotvacuum.collision.Collision;

import java.util.Optional;
import java.util.Set;

public class ActualMovement {
    private final ProposedMovement proposedMovement;
    private final Optional<Movement> actualMovement;
    private final Set<Collision> collisions;

    public ActualMovement(ProposedMovement proposedMovement, Optional<Movement> actualMovement, Set<Collision> collisions) {
        this.proposedMovement = proposedMovement;
        this.actualMovement = actualMovement;
        this.collisions = collisions;
    }

    public ProposedMovement getProposedMovement() {
        return proposedMovement;
    }

    public Optional<Movement> getMovement() {
        return actualMovement;
    }

    public Set<Collision> getCollisions() {
        return collisions;
    }
}
