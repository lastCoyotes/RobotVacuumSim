package robotvacuum.robot;

import robotvacuum.collision.PositionWithRotation;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author Tristan Boler
 */
public class RobotSimulationState implements Serializable {

    private PositionWithRotation positionWithRotation;
    private double remainingBattery;
    private ActualMovement previousMovement = null;
    private ActualMovement currentMovement = null;
    private double distanceAlongMovement = 0.0;

    public RobotSimulationState(PositionWithRotation positionWithRotation, double remainingBattery) {
        this.positionWithRotation = positionWithRotation;
        this.remainingBattery = remainingBattery;
    }

    public void updatePosition(final Movement mov) {
        if (!mov.fixedDistancePositionWithRotation(distanceAlongMovement).isCloseTo(this.positionWithRotation)) {
            throw new IllegalArgumentException("movement does not start at current robot position");
        }
        this.positionWithRotation = mov.getStopPosWithRotation();
    }

    public void updatePosition(final Movement mov, final double distanceToMove) {
        if (!mov.fixedDistancePositionWithRotation(distanceAlongMovement).equals(this.positionWithRotation)) {
            throw new IllegalArgumentException("movement does not start at current robot position");
        }
        if (distanceToMove <= 0.0) {
            throw new IllegalArgumentException("must move forwards");
        }
        if (distanceToMove + distanceAlongMovement > mov.totalTravelDistance()) {
            throw new IllegalArgumentException("cannot move past the end of the movement");
        }
        this.positionWithRotation = mov.fixedDistancePositionWithRotation(distanceToMove + distanceAlongMovement);
    }

    public void updatePosition(ActualMovement mov) {
        mov.getMovement().ifPresent(this::updatePosition);
        this.currentMovement = null;
        this.previousMovement = mov;
    }

    public void updateMovement(ActualMovement mov) {
        this.currentMovement = mov;
        this.updatePosition(mov);
    }

    public void updateMovement(ActualMovement mov, double distanceToMove) {
        mov.getMovement().ifPresent(x -> this.updatePosition(x, distanceToMove));
        this.currentMovement = null;
        this.previousMovement = mov;
    }

    /**
     * @return the position
     */
    public PositionWithRotation getPositionWithRotation() {
        return positionWithRotation;
    }

    /**
     * @param positionWithRotation the positionWithRotation to set
     */
    public void setPositionWithRotation(PositionWithRotation positionWithRotation) {
        this.positionWithRotation = positionWithRotation;
    }

    /**
     * @return the remainingBattery
     */
    public double getRemainingBattery() {
        return remainingBattery;
    }

    /**
     * @param remainingBattery the remainingBattery to set
     */
    public void setRemainingBattery(double remainingBattery) {
        this.remainingBattery = remainingBattery;
    }

    public Optional<ActualMovement> getPreviousMovement() { return Optional.ofNullable(this.previousMovement); }

}
