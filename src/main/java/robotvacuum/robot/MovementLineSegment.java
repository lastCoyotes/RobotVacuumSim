package robotvacuum.robot;

import robotvacuum.collision.Position;
import robotvacuum.collision.PositionWithRotation;

public class MovementLineSegment implements Movement {

    private final Position startPos;
    private final Position stopPos;

    public MovementLineSegment(Position startPos, Position stopPos) {
        this.startPos = startPos;
        this.stopPos = stopPos;
    }

    @Override
    public PositionWithRotation linearInterpolatedPositionWithRotation(double percent) {
        if (!(0 <= percent && percent <= 1)) {
            throw new IllegalArgumentException("Percent out of bounds. Value: " + percent);
        }
        return new PositionWithRotation(startPos.linearInterpolateTo(stopPos, percent), startPos.directionTo(stopPos));
    }

    /**
     * @param distance distance from start position along movement in meters
     * @return the position distance from the start along the path
     * @throws RuntimeException if the distance is greater than the total movement distance
     */
    @Override
    public PositionWithRotation fixedDistancePositionWithRotation(double distance) {
        return linearInterpolatedPositionWithRotation(distance / totalTravelDistance());
    }

    /**
     * @param distance distance of the original movement that the new movement will cover in meters
     * @return A new movement (or subclass) object that covers distance amount of the original movement
     * @throws Exception if the distance is less than 0 or greater than the total distance
     */
    @Override
    public MovementLineSegment partialFixedDistanceMovement(double distance) {
        return new MovementLineSegment(startPos, fixedDistancePositionWithRotation(distance).getPos());
    }

    @Override
    public MovementLineSegment partialLinearInterpolatedMovement(double percent) {
        return new MovementLineSegment(startPos, linearInterpolatedPositionWithRotation(percent).getPos());
    }

    /**
     * @return the distance travelled from the center of the start position along the movement to the final position
     */
    @Override
    public double totalTravelDistance() {
        return startPos.distanceTo(stopPos);
    }

    @Override
    public double getFinalFacingDirection() {
        return startPos.directionTo(stopPos);
    }

    /**
     * @return the startPos
     */
    public Position getStartPos() {
        return startPos;
    }

    /**
     * @return the stopPos
     */
    @Override
    public PositionWithRotation getStopPosWithRotation() {
        return new PositionWithRotation(stopPos, startPos.directionTo(stopPos));
    }

}
