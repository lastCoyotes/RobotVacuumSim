package robotvacuum.robot;

import robotvacuum.collision.Position;
import robotvacuum.collision.PositionWithRotation;
import robotvacuum.utility.MathHelper;

public class MovementSpiral implements Movement {

    //    private final Position startPos;
    private final double referenceAngle;
    private final double startAngle;
    private final Position spiralCenter;
    private final double linearOffsetPerRotation;
    private final double totalRotation;
    private final CircleDirection circleDirection;
    private final double startingDistanceFromCenter;
    private final double totalTravelDistance;
    private final MathHelper mathHelper;

    public MovementSpiral(final double referenceAngle, final double startAngle, final double startingDistanceFromCenter,
                          final Position spiralCenter, final double totalRotation, final double linearOffsetPerRotation,
                          final CircleDirection circleDirection) {
        this(referenceAngle, startAngle, startingDistanceFromCenter, spiralCenter, totalRotation,
                linearOffsetPerRotation, circleDirection, new MathHelper());
    }

    public MovementSpiral(final double referenceAngle, final double startAngle, final double startingDistanceFromCenter,
                          final Position spiralCenter, final double totalRotation, final double linearOffsetPerRotation,
                          final CircleDirection circleDirection, MathHelper mathHelper) {
        if (startAngle < referenceAngle) {
            throw new IllegalArgumentException("start angle cannot be less than reference angle");
        }
        this.referenceAngle = referenceAngle;
        this.startAngle = startAngle;
        this.spiralCenter = spiralCenter;
        this.linearOffsetPerRotation = linearOffsetPerRotation;
        this.totalRotation = totalRotation;
        this.circleDirection = circleDirection;
        this.startingDistanceFromCenter = startingDistanceFromCenter;
        this.mathHelper = mathHelper;

        this.totalTravelDistance = arcLengthWithOffset(startAngle, totalRotation, startingDistanceFromCenter);
    }

    private double arcLengthWithOffset(double startAngle, double totalRotation, double a) {
        return arcLengthZeroOffset(totalRotation + startAngle, a) - arcLengthZeroOffset(startAngle, a);
    }

    /**
     * wikipedia algorithm for arc length of archimedean spiral
     * https://en.wikipedia.org/wiki/Archimedean_spiral
     *
     * @param totalRotation degrees to turn from start
     * @param a
     * @return
     */
    private double arcLengthZeroOffset(double totalRotation, double a) {
        final double totalTravelDistance;
        double subValue = Math.sqrt(1 + (totalRotation * totalRotation));
        return (a / 2) * (totalRotation * subValue + Math.log(totalRotation + subValue));
    }

    public Position linearInterpolatedPosition(double percent) {
        if (!(0 <= percent && percent <= 1)) {
            throw new IllegalArgumentException("Percent out of bounds. Value: " + percent);
        }
        double finalAngle = (totalRotation * percent) + startAngle - referenceAngle;
        return spiralCenter.offsetPositionPolar((totalRotation * percent) + startAngle,
                startingDistanceFromCenter + (finalAngle * linearOffsetPerRotation));
    }

    /**
     * @param percent the percent along the movement
     * @return the position that is percent along the movement from the start
     * @throws IllegalArgumentException if the percent is less than 0 or greater than 1
     */
    @Override
    public PositionWithRotation linearInterpolatedPositionWithRotation(double percent) {
        if (!(0 <= percent && percent <= 1)) {
            throw new IllegalArgumentException("Percent out of bounds. Value: " + percent);
        }
        Position pos = linearInterpolatedPosition(percent);
        return new PositionWithRotation(pos, getFacingDirectionLinearInterpolated(pos, percent));
    }

    private Position fixedDistancePosition(double distance) {
        return linearInterpolatedPosition(distance / totalTravelDistance());
    }

    /**
     * @param distance distance from start position along movement in meters
     * @return the position distance from the start along the path
     * @throws IllegalArgumentException if the distance is greater than the total movement distance
     */
    @Override
    public PositionWithRotation fixedDistancePositionWithRotation(double distance) {
        return linearInterpolatedPositionWithRotation(distance / totalTravelDistance());
    }

    /**
     * @param percent percent of the original movement that the new movement will cover
     * @return A new movement (or subclass) object that covers percent amount of the original movement
     * @throws IllegalArgumentException if the percent is less than 0 or greater than 1
     */
    @Override
    public Movement partialLinearInterpolatedMovement(double percent) {
        if (!(0 <= percent && percent <= 1)) {
            throw new IllegalArgumentException("Percent out of bounds. Value: " + percent);
        }
        return new MovementSpiral(referenceAngle, startAngle, startingDistanceFromCenter, spiralCenter,
                percent * totalRotation, linearOffsetPerRotation, circleDirection);
    }

    /**
     * @param distance distance of the original movement that the new movement will cover in meters
     * @return A new movement (or subclass) object that covers distance amount of the original movement
     * @throws IllegalArgumentException if the distance is less than 0 or greater than the total distance
     */
    @Override
    public Movement partialFixedDistanceMovement(double distance) {
        return partialLinearInterpolatedMovement(distance / totalTravelDistance());
    }

    /**
     * @return the distance travelled from the start position along the movement to the final position
     */
    @Override
    public double totalTravelDistance() {
        return totalTravelDistance;
    }

    @Override
    public Position getStartPos() {
        double angleDiff = startAngle - referenceAngle;
        return spiralCenter.offsetPositionPolar(startAngle, startingDistanceFromCenter + (angleDiff * linearOffsetPerRotation));
    }

    @Override
    public PositionWithRotation getStopPosWithRotation() {
        return linearInterpolatedPositionWithRotation(1.0);
    }

    @Override
    public double getFinalFacingDirection() {
        if (this.circleDirection == CircleDirection.CLOCKWISE) {
            final double endRotation = totalRotation + startAngle - referenceAngle;
            return mathHelper.normalizeAngle(endRotation + (Math.PI / 2));
        } else {
            final double endRotation = startAngle - referenceAngle - totalRotation;
            return mathHelper.normalizeAngle(endRotation - (Math.PI / 2));
        }
    }

    public MovementSpiral continueSpiral(double continueAngle) {
        return new MovementSpiral(referenceAngle, totalRotation + startAngle, startingDistanceFromCenter,
                spiralCenter, continueAngle, linearOffsetPerRotation, circleDirection);
    }

    public double getFacingDirectionFixedDistance(double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance cannot be less than 0");
        } else if (distance > totalTravelDistance) {
            throw new IllegalArgumentException("distance cannot be greater than the arcDistance");
        }
        double polarSlopeAngle = 1.0 / (startAngle + totalRotation);
        Position position = fixedDistancePosition(distance);
        double angle = this.spiralCenter.directionTo(position);

        return angle + polarSlopeAngle;
    }

    public double getFacingDirectionLinearInterpolated(double percent) {
        return getFacingDirectionFixedDistance(percent * totalTravelDistance);
    }

    private double getFacingDirectionFixedDistance(Position position, double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance cannot be less than 0");
        } else if (distance > totalTravelDistance) {
            throw new IllegalArgumentException("distance cannot be greater than the arcDistance");
        }
        double polarSlopeAngle = 1.0 / (startAngle + totalRotation);
        double angle = this.spiralCenter.directionTo(position);

        return angle + polarSlopeAngle;
    }

    private double getFacingDirectionLinearInterpolated(Position position, double percent) {
        return getFacingDirectionFixedDistance(position, percent * totalTravelDistance);
    }
}
