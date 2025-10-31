package robotvacuum.robot;

import robotvacuum.collision.Collision;
import robotvacuum.collision.Position;
import robotvacuum.utility.MathHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

public class SpiralVacuumStrategy implements VacuumStrategy, Serializable {

    private final double minSpiralWallDistance;
    private final double maxSpiralWallDistance;
    private final double collisionSearchDistance;
    private final double previousDirection;
    private final Random random;
    private final MathHelper mathHelper;
    private SpiralState spiralState;
    private MovementSpiral previousSpiral;

    public SpiralVacuumStrategy(final double minSpiralWallDistance, final double maxSpiralWallDistance,
                                final double collisionSearchDistance, final double startingDirection, final long seed) {
        this.minSpiralWallDistance = minSpiralWallDistance;
        this.maxSpiralWallDistance = maxSpiralWallDistance;
        this.collisionSearchDistance = collisionSearchDistance;
        this.previousDirection = startingDirection;
        this.random = new Random(seed);
        this.mathHelper = new MathHelper();
        this.spiralState = SpiralState.LINE_FOR_COLLISION;
    }

    public SpiralVacuumStrategy(final double minSpiralWallDistance, final double maxSpiralWallDistance,
                                final double collisionSearchDistance, final long seed) {
        this(minSpiralWallDistance, maxSpiralWallDistance, collisionSearchDistance, 0, seed);
    }

    public SpiralVacuumStrategy(final double minSpiralWallDistance, final double maxSpiralWallDistance,
                                final double collisionSearchDistance) {
        this(minSpiralWallDistance, maxSpiralWallDistance, collisionSearchDistance, 0, 0);
    }

    @Override
    public ProposedMovement vacuum(RobotSimulationState rSimState) {
        Optional<ActualMovement> previousMovement = rSimState.getPreviousMovement();
        Collection<Collision> previousCollisions = previousMovement
                .map(actualMovement -> actualMovement.getCollisions())
                .orElse(Collections.emptySet());

        Position robotPosition = rSimState.getPositionWithRotation().getPos();
        if (!previousCollisions.isEmpty()) {
            this.spiralState = SpiralState.LINE_FOR_SPIRAL;
        }

        if (this.spiralState == SpiralState.LINE_FOR_COLLISION) {
            return new ProposedMovement(
                    new MovementLineSegment(
                            robotPosition,
                            robotPosition.offsetPositionPolar(previousDirection, collisionSearchDistance)));
        } else if (this.spiralState == SpiralState.LINE_FOR_SPIRAL) {
            if (previousCollisions.isEmpty()) {
                this.spiralState = SpiralState.START_SPIRAL;
                return vacuum(rSimState);
            } else {
                double minPossibleDirection =
                        previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).map(x -> x + Math.PI / 2).max().getAsDouble();

                double maxPossibleDirection =
                        previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).map(x -> x - Math.PI / 2).min().getAsDouble()
                                + 2 * Math.PI;

                double direction = mathHelper.normalizeAngle((random.nextDouble() * (maxPossibleDirection - minPossibleDirection)) + minPossibleDirection);
                double distance = (random.nextDouble() * (maxSpiralWallDistance - minSpiralWallDistance)) + minSpiralWallDistance;

                return new ProposedMovement(
                        new MovementLineSegment(
                                robotPosition,
                                robotPosition.offsetPositionPolar(direction, distance)));
            }
        } else if (this.spiralState == SpiralState.START_SPIRAL) {
            final double distance = 0.1;
            final Position spiralCenter = robotPosition.offsetPositionPolar(rSimState.getPositionWithRotation().getRot() + Math.PI / 2, distance);
            final double referenceAngle = mathHelper.normalizeAngle(rSimState.getPositionWithRotation().getRot() - Math.PI / 2);

            MovementSpiral newSpiral = new MovementSpiral(referenceAngle, referenceAngle, distance, spiralCenter,
                    Math.PI * 2, 0.15, CircleDirection.CLOCKWISE);
            previousSpiral = newSpiral;
            this.spiralState = SpiralState.CONTINUE_SPIRAL;
            return new ProposedMovement(newSpiral);
        } else if (this.spiralState == SpiralState.CONTINUE_SPIRAL) {
            MovementSpiral newSpiral = previousSpiral.continueSpiral(Math.PI * 2);
            previousSpiral = newSpiral;
            return new ProposedMovement(newSpiral);
        } else {
            throw new UnsupportedOperationException("invalid spiral state");
        }
    }
}

enum SpiralState {
    LINE_FOR_SPIRAL,
    LINE_FOR_COLLISION,
    START_SPIRAL,
    CONTINUE_SPIRAL
}