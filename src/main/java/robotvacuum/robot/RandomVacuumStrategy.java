package robotvacuum.robot;


import robotvacuum.collision.Collision;
import robotvacuum.collision.Position;
import robotvacuum.utility.MathHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.io.Serializable;

public class RandomVacuumStrategy implements VacuumStrategy, Serializable {

    private final double defaultVacuumDistance;
    private double previousDirection = 0.0;
    private final Random random;
    private final MathHelper mathHelper;

    public RandomVacuumStrategy(long seed, double defaultVacuumDistance) {
        this.defaultVacuumDistance = defaultVacuumDistance;
        this.random = new Random(seed);
        this.mathHelper = new MathHelper();
    }

    public RandomVacuumStrategy(long seed, double defaultVacuumDistance, double startingDirection) {
        this(seed, defaultVacuumDistance);
        this.previousDirection = startingDirection;
    }

    @Override
    public ProposedMovement vacuum(RobotSimulationState rSimState) {
        Optional<ActualMovement> previousMovement = rSimState.getPreviousMovement();
        Collection<Collision> previousCollisions = previousMovement
                .map(ActualMovement::getCollisions)
                .orElse(Collections.emptySet());

        Position pos = rSimState.getPositionWithRotation().getPos();
        if (previousCollisions.isEmpty()) {
//            rSimState.setFacingDirection(previousDirection);
            return new ProposedMovement(
                    new MovementLineSegment(
                            pos,
                            pos.offsetPositionPolar(previousDirection, defaultVacuumDistance)));
        } else {
            double minPossibleDirection =
                    previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).map(x -> x + Math.PI / 2).max().getAsDouble();

            double maxPossibleDirection =
                    previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).map(x -> x - Math.PI / 2).min().getAsDouble()
                            + 2 * Math.PI;

            double direction = mathHelper.normalizeAngle((random.nextDouble() * (maxPossibleDirection - minPossibleDirection)) + minPossibleDirection);

            this.previousDirection = direction;

//            rSimState.setFacingDirection(direction);
            return new ProposedMovement(
                    new MovementLineSegment(pos, pos.offsetPositionPolar(direction, defaultVacuumDistance)));
        }
    }

}
