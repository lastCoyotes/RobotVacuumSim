package robotvacuum.robot;

import robotvacuum.collision.Position;
import robotvacuum.collision.PositionWithRotation;

/**
 * @author Tristan Boler
 */
public interface Movement {


    /**
     * @param percent the percent along the movement
     * @return the position that is percent along the movement from the start
     * @throws IllegalArgumentException if the percent is less than 0 or greater than 1
     */
    PositionWithRotation linearInterpolatedPositionWithRotation(double percent);

    /**
     * @param distance distance from start position along movement in meters
     * @return the position distance from the start along the path
     * @throws IllegalArgumentException if the distance is greater than the total movement distance
     */
    PositionWithRotation fixedDistancePositionWithRotation(double distance);

    /**
     * @param percent percent of the original movement that the new movement will cover
     * @return A new movement (or subclass) object that covers percent amount of the original movement
     * @throws IllegalArgumentException if the percent is less than 0 or greater than 1
     */
    Movement partialLinearInterpolatedMovement(double percent);

    /**
     * @param distance distance of the original movement that the new movement will cover in meters
     * @return A new movement (or subclass) object that covers distance amount of the original movement
     * @throws IllegalArgumentException if the distance is less than 0 or greater than the total distance
     */
    Movement partialFixedDistanceMovement(double distance);

    /**
     * @return the distance travelled from the start position along the movement to the final position
     */
    double totalTravelDistance();

    Position getStartPos();
    PositionWithRotation getStopPosWithRotation();
    double getFinalFacingDirection();
}
