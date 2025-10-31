package robotvacuum.robot;

import robotvacuum.collision.Collision;
import robotvacuum.collision.Position;
import robotvacuum.collision.PositionWithRotation;

import java.util.Collection;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

public class SnakeVacuumStrategy implements VacuumStrategy, Serializable {

    private final double defaultVacuumDistance;
    private double previousDirection = 0.0, movedNextToWall = 0.0, nextDirection = 0.0, collisionDirection = 0.0;
    private boolean nextToWall = false, flip = false;

    public SnakeVacuumStrategy(double defaultVacuumDistance, double startingDirection) {
        this.defaultVacuumDistance = defaultVacuumDistance;
        this.previousDirection = startingDirection;
    }
    
    @Override
    public ProposedMovement vacuum(RobotSimulationState rSimState) {
        Optional<ActualMovement> previousMovement = rSimState.getPreviousMovement();
        Collection<Collision> previousCollisions = previousMovement
                .map(actualMovement -> actualMovement.getCollisions())
                .orElse(Collections.emptySet());

        Position pos = rSimState.getPositionWithRotation().getPos();
        if (previousCollisions.isEmpty() && nextToWall) {
            movedNextToWall = movedNextToWall + defaultVacuumDistance;
            if (movedNextToWall >= 0.25) {
                //moved a distance equal to the vacuum's diameter, move across room again
                double direction = nextDirection;
                previousDirection = direction;
                nextToWall = false;
                movedNextToWall = 0;
                
//                rSimState.setFacingDirection(direction);
                return new ProposedMovement(
                        new MovementLineSegment(
                                pos,
                                pos.offsetPositionPolar(direction, defaultVacuumDistance)));
            }
//            rSimState.setFacingDirection(previousDirection);
            return new ProposedMovement(
                    new MovementLineSegment(
                            pos,
                            pos.offsetPositionPolar(previousDirection, defaultVacuumDistance)));
        } else if (previousCollisions.isEmpty() && !nextToWall) {
//            rSimState.setFacingDirection(previousDirection);
            return new ProposedMovement(
                    new MovementLineSegment(
                            pos,
                            pos.offsetPositionPolar(previousDirection, defaultVacuumDistance)));
        } else if (nextToWall) {
            //reached corner, change direction
            movedNextToWall = 0;
            double direction = previousDirection + Math.PI;
            nextDirection = collisionDirection - (4*Math.PI)/6;
            previousDirection = direction;
            
//            rSimState.setFacingDirection(direction);
            return new ProposedMovement(
                    new MovementLineSegment(
                            pos,
                            pos.offsetPositionPolar(direction, defaultVacuumDistance)));
        } else {
            //hit wall, travel along it for a bit
            collisionDirection = previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).max().getAsDouble();
            double direction = collisionDirection - Math.PI/2;
            if (flip) {
                direction = direction + Math.PI;
            }
            flip = !flip;
            nextDirection = previousDirection + Math.PI;
            previousDirection = direction;
            nextToWall = true;
            
//            rSimState.setFacingDirection(direction);
            return new ProposedMovement(
                    new MovementLineSegment(
                            pos,
                            pos.offsetPositionPolar(direction, defaultVacuumDistance)));
        }
    }
}