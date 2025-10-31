package robotvacuum.collision;

import robotvacuum.house.House;
import robotvacuum.robot.ActualMovement;
import robotvacuum.robot.Movement;
import robotvacuum.robot.ProposedMovement;
import robotvacuum.robot.RobotVacuum;
import robotvacuum.robot.VacuumStrategy;
import robotvacuum.utility.MathHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tristan Boler
 */
public class CollisionDetector implements Serializable {

    private final MathHelper mathHelper;

    public CollisionDetector() {
        this(new MathHelper());
    }

    public CollisionDetector(MathHelper mathHelper) {
        this.mathHelper = mathHelper;
    }

    public Optional<Collision> detectStaticCollision(CollisionTestData<? extends CollisionShape> ctd1, CollisionTestData<? extends CollisionShape> ctd2) {
        if (Shape.CIRCLE == ctd1.getcShape().getShape() && Shape.CIRCLE == ctd2.getcShape().getShape()) {
            return detectStaticCollisionBetweenCircles((CollisionTestData<CollisionCircle>) ctd1, (CollisionTestData<CollisionCircle>) ctd2);
        } else if (Shape.CIRCLE == ctd1.getcShape().getShape() && Shape.RECTANGLE == ctd2.getcShape().getShape()) {
            return detectStaticCollisionBetweenCircleAndRectangle((CollisionTestData<CollisionCircle>) ctd1, (CollisionTestData<CollisionRectangle>) ctd2);
        } else if (Shape.RECTANGLE == ctd1.getcShape().getShape() && Shape.CIRCLE == ctd2.getcShape().getShape()) {
            return detectStaticCollisionBetweenRectangleAndCircle((CollisionTestData<CollisionRectangle>) ctd1, (CollisionTestData<CollisionCircle>) ctd2);
        } else if (Shape.RECTANGLE == ctd1.getcShape().getShape() && Shape.RECTANGLE == ctd2.getcShape().getShape()) {
            return detectStaticCollisionBetweenRectangles((CollisionTestData<CollisionRectangle>) ctd1, (CollisionTestData<CollisionRectangle>) ctd2);
        }
        throw new UnsupportedOperationException("Can only detect collisions between a combination of rectangles and circles");
    }

    public Set<Collision> detectStaticCollision(CollisionTestData<? extends CollisionShape> ctd1, Set<CollisionTestData<? extends CollisionShape>> ctds) {
        return ctds.stream()
                .flatMap(ctd -> detectStaticCollision(ctd1, ctd).stream())
                .collect(Collectors.toSet());
    }

    public Set<Collision> detectStaticCollision(RobotVacuum<? extends VacuumStrategy> rv, House house) {
        return detectStaticCollision(
                new CollisionTestData<>(
                        rv.getrSimState().getPositionWithRotation().getPos(),
                        rv.getProperties().getcCircle()
                ),
                house.getRooms().entrySet().stream().flatMap(
                        entry -> entry
                                .getValue()
                                .getWalls()
                                .entrySet()
                                .stream()
                                .map(wallEntry -> new CollisionTestData<>(entry.getKey().offsetPositionCartesian(wallEntry.getKey()),
                                        wallEntry.getValue().getcRect()))
                ).collect(Collectors.toSet()));
    }

    /**
     * positions are expected to be the top left corner
     *
     * @param rawCtd1
     * @param rawCtd2
     * @return
     */
    Optional<Collision> detectStaticCollisionBetweenRectangles(CollisionTestData<CollisionRectangle> rawCtd1, CollisionTestData<CollisionRectangle> rawCtd2) {
        final CollisionTestData<CollisionRectangle> ctd1 = mathHelper.changeTopLeftCoordinatesToCenter(rawCtd1);
        final CollisionTestData<CollisionRectangle> ctd2 = mathHelper.changeTopLeftCoordinatesToCenter(rawCtd2);

        final CollisionRectangle cRect1 = ctd1.getcShape();
        final CollisionRectangle cRect2 = ctd2.getcShape();
        Position ctd1Pos = ctd1.getPos();
        Position ctd2Pos = ctd2.getPos();
        //max distance that still has potential overlap
        final double xOverlap = (cRect1.getWidth() / 2) + (cRect2.getWidth() / 2);
        final double yOverlap = (cRect1.getHeight() / 2) + (cRect2.getHeight() / 2);
        //diffs between object centers
        final double xDiff = ctd1Pos.xDiff(ctd2Pos);
        final double yDiff = ctd1Pos.yDiff(ctd2Pos);

        //if there is overlap (collision) somewhere
        if (Math.abs(xDiff) <= xOverlap && Math.abs(yDiff) <= yOverlap) {
            //get positions of sides of the overlap rectangle
            double leftSideX = Math.max(ctd1Pos.getX() - (cRect1.getWidth() / 2), ctd2Pos.getX() - (cRect2.getWidth() / 2));
            double rightSideX = Math.min(ctd1Pos.getX() + (cRect1.getWidth() / 2), ctd2Pos.getX() + (cRect2.getWidth() / 2));
            double topSideY = Math.min(ctd1Pos.getY() + (cRect1.getHeight() / 2), ctd2Pos.getY() + (cRect2.getHeight() / 2));
            double bottomSideY = Math.max(ctd1Pos.getY() - (cRect1.getHeight() / 2), ctd2Pos.getY() - (cRect2.getHeight() / 2));

            Position overlapRectangleCenter = new Position((leftSideX + rightSideX) / 2, (topSideY + bottomSideY) / 2);

            return Optional.of(new Collision(
                    overlapRectangleCenter,
                    ctd1Pos.directionTo(overlapRectangleCenter),
                    ctd1,
                    ctd2));
        }
        return Optional.empty();
    }

    Optional<Collision> detectStaticCollisionBetweenCircleAndRectangle(CollisionTestData<CollisionCircle> ctd1, CollisionTestData<CollisionRectangle> rawCtd2) {
        final double circleRadius = ctd1.getcShape().getRadius();
        final CollisionTestData<CollisionRectangle> ctd2 = mathHelper.changeTopLeftCoordinatesToCenter(rawCtd2);
        final CollisionRectangle cRect = ctd2.getcShape();
        final double xOverlap = circleRadius + (cRect.getWidth() / 2);
        final double yOverlap = circleRadius + (cRect.getHeight() / 2);
        final double xDiff = ctd1.getPos().xDiff(ctd2.getPos());
        final double yDiff = ctd1.getPos().yDiff(ctd2.getPos());

        //if potential x or y overlap
        if (Math.abs(xDiff) <= xOverlap && Math.abs(yDiff) <= yOverlap) {
            //if above or below no corner
            if (Math.abs(xDiff) <= (cRect.getWidth() / 2) || Math.abs(yDiff) <= (cRect.getHeight() / 2)) {
                final double lowerBound = Math.min(ctd1.getPos().getY() + circleRadius, ctd2.getPos().getY() + (cRect.getHeight() / 2));
                final double upperBound = Math.max(ctd1.getPos().getY() - circleRadius, ctd2.getPos().getY() - (cRect.getHeight() / 2));
                final double collisionYPos = (lowerBound + upperBound) / 2.0;

                final double leftBound = Math.max(ctd1.getPos().getX() - circleRadius, ctd2.getPos().getX() - (cRect.getWidth() / 2));
                final double rightBound = Math.min(ctd1.getPos().getX() + circleRadius, ctd2.getPos().getX() + (cRect.getWidth() / 2));
                final double collisionXPos = (leftBound + rightBound) / 2.0;

                final Position collisionPosition = new Position(collisionXPos, collisionYPos);
                final double collisionDirection = ctd1.getPos().directionTo(collisionPosition);

                return Optional.of(new Collision(collisionPosition, collisionDirection, ctd1, ctd2));

                //if in corner
            } else {
                final Position cornerPos = new Position(
                        ctd1.getPos().getX() + (-Math.signum(xDiff) * cRect.getWidth() / 2),
                        ctd1.getPos().getY() + (-Math.signum(yDiff) * cRect.getHeight() / 2)
                );

                if (ctd1.getPos().distanceTo(cornerPos) <= circleRadius) {
                    return Optional.of(new Collision(cornerPos, ctd1.getPos().directionTo(cornerPos), ctd1, ctd2));
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    Optional<Collision> detectStaticCollisionBetweenRectangleAndCircle(CollisionTestData<CollisionRectangle> ctd1, CollisionTestData<CollisionCircle> ctd2) {
        //call the other function with swapped arguments
        return detectStaticCollisionBetweenCircleAndRectangle(ctd2, ctd1)
                .map((Collision col) -> new Collision(col.getCollisionPosition(),
                        //swap the collision direction but make sure it stays in -PI to PI
                        mathHelper.normalizeAngle(col.getCollisionDirection() + Math.PI),
                        ctd1, ctd2));
    }

    Optional<Collision> detectStaticCollisionBetweenCircles(CollisionTestData<CollisionCircle> ctd1, CollisionTestData<CollisionCircle> ctd2) {
        double circle1Radius = ctd1.getcShape().getRadius();
        double circle2Radius = ctd2.getcShape().getRadius();
        double radiusSum = circle1Radius + circle2Radius;

        double distanceBetweenCenters = ctd1.getPos().distanceTo(ctd2.getPos());

        if (distanceBetweenCenters <= radiusSum) {
            double relativeSize = circle1Radius / radiusSum;
            Position collisionPosition = ctd1.getPos().linearInterpolateTo(ctd2.getPos(), relativeSize);
            double collisionDirection = ctd1.getPos().directionTo(ctd2.getPos());

            return Optional.of(new Collision(collisionPosition, collisionDirection, ctd1, ctd2));
        }
        return Optional.empty();
    }

    public ActualMovement detectDynamicCollision(CollisionTestData<? extends CollisionShape> ctd1,
                                                 CollisionTestData<? extends CollisionShape> ctd2,
                                                 ProposedMovement proposedMovement) {
        return detectDynamicCollision(ctd1, Set.of(ctd2), proposedMovement);
    }

    public ActualMovement detectDynamicCollision(CollisionTestData<? extends CollisionShape> ctd1,
                                                 Set<CollisionTestData<? extends CollisionShape>> ctds,
                                                 ProposedMovement proposedMovement) {
        //if continue colliding
        Set<Collision> startCollisions = detectStaticCollision(ctd1, ctds);
        if (!startCollisions.isEmpty()) {
            double minDistance = Math.min(0.001, proposedMovement.getMov().totalTravelDistance());
            Position collisionTestPosition = proposedMovement.getMov().fixedDistancePositionWithRotation(minDistance).getPos();
            Set<Collision> movementCollisions = detectStaticCollision(new CollisionTestData<>(collisionTestPosition, ctd1.getcShape()), ctds);


            //if movement would continue collision
            if (!movementCollisions.isEmpty()) {
                //already colliding and movement continues colliding
                return new ActualMovement(proposedMovement, Optional.empty(), startCollisions);
            }
        }

        //check every centimeter for a collision
        double initialCheckResolution = 0.01;
        //millimeter finalCheckResolution
        double finalCheckResolution = 0.001;
        for (double travelDistance = initialCheckResolution;
             travelDistance <= proposedMovement.getMov().totalTravelDistance();
             travelDistance += initialCheckResolution) {
            Set<Collision> testCollisions = detectStaticCollision(
                    new CollisionTestData<>(proposedMovement.getMov().fixedDistancePositionWithRotation(travelDistance).getPos(), ctd1.getcShape()),
                    ctds);

            //if there is a collision
            if (!testCollisions.isEmpty()) {

                //get a more accurate collision
                MovementCollisions finalCollision = binarySearchDynamicCollision(
                        ctd1, ctds, proposedMovement, travelDistance - initialCheckResolution, travelDistance,
                        testCollisions, finalCheckResolution);

                //create and return the actual resulting movement to the collision
                Optional<Movement> movement;
                if (finalCollision.getNoCollisionDistance() > 0.0) {
                    movement = Optional.of(proposedMovement.getMov().partialFixedDistanceMovement(finalCollision.getNoCollisionDistance()));
                } else {
                    movement = Optional.empty();
                }
                return new ActualMovement(
                        proposedMovement,
                        movement,
                        finalCollision.getCollisions());
            }
        }

        Set<Collision> testCollisions = detectStaticCollision(
                new CollisionTestData<>(proposedMovement.getMov().getStopPosWithRotation().getPos(), ctd1.getcShape()),
                ctds);

        //if there is a collision
        final double fullDistance = proposedMovement.getMov().totalTravelDistance();
        final double noCollisionDistance = fullDistance - (fullDistance % initialCheckResolution);
        if (!testCollisions.isEmpty()) {

            //get a more accurate collision
            MovementCollisions finalCollision = binarySearchDynamicCollision(
                    ctd1, ctds, proposedMovement, noCollisionDistance, fullDistance,
                    testCollisions, finalCheckResolution);

            //create and return the actual resulting movement to the collision
            return new ActualMovement(
                    proposedMovement,
                    Optional.of(proposedMovement.getMov().partialFixedDistanceMovement(finalCollision.getNoCollisionDistance())),
                    finalCollision.getCollisions());
        }

        //no collision detected, so the actual movement is the entire proposed movement
        return new ActualMovement(proposedMovement, Optional.of(proposedMovement.getMov()), Collections.emptySet());
    }

    public ActualMovement detectDynamicCollision(RobotVacuum<? extends VacuumStrategy> rv, House house, ProposedMovement proposedMovement) {
        Stream<CollisionTestData<? extends CollisionShape>> wallCollisionTestDataStream = house.getRooms().entrySet().stream().flatMap(
                entry -> entry
                        .getValue()
                        .getWalls()
                        .entrySet()
                        .stream()
                        .map(wallEntry -> new CollisionTestData<>(entry.getKey().offsetPositionCartesian(wallEntry.getKey()),
                                wallEntry.getValue().getcRect())));


        Stream<CollisionTestData<? extends CollisionShape>> furnitureCollisionTestDataStream = house.getRooms().values().stream()
                .flatMap(room -> room.getAllFurniture().stream().flatMap(furniture -> furniture.getCollisionTestData().stream()));

        return detectDynamicCollision(
                new CollisionTestData<>(rv.getrSimState().getPositionWithRotation().getPos(), rv.getProperties().getcCircle()),
                Stream.concat(wallCollisionTestDataStream, furnitureCollisionTestDataStream)
                        .collect(Collectors.toSet()),
                proposedMovement);
    }

    MovementCollisions binarySearchDynamicCollision(
            CollisionTestData<? extends CollisionShape> ctd1,
            CollisionTestData<? extends CollisionShape> ctd2,
            ProposedMovement proposedMovement,
            double distanceToNoCollision,
            double distanceToCollision,
            Collision collision,
            double resolution) {
        return binarySearchDynamicCollision(ctd1, Set.of(ctd2), proposedMovement, distanceToNoCollision, distanceToCollision, Set.of(collision), resolution);
    }

    MovementCollisions binarySearchDynamicCollision(
            CollisionTestData<? extends CollisionShape> ctd1,
            Set<CollisionTestData<? extends CollisionShape>> ctds,
            ProposedMovement proposedMovement,
            double distanceToNoCollision,
            double distanceToCollision,
            Set<Collision> collisions,
            double resolution) {
        double startOffset = distanceToNoCollision;
        double halfway = (distanceToNoCollision + distanceToCollision) / 2;

        try {
            //if we would move less than the given resolution, exit search and return collisions
            //recursive exit condition
            double potentialMovement = (halfway - startOffset) / 2;
            if (potentialMovement < resolution) {
                return new MovementCollisions(collisions, distanceToCollision, distanceToNoCollision);
            }

            Position halfwayPosition = proposedMovement.getMov().fixedDistancePositionWithRotation(halfway).getPos();
            Set<Collision> halfwayCollisions =
                    detectStaticCollision(new CollisionTestData<>(halfwayPosition, ctd1.getcShape()), ctds);
            if (!halfwayCollisions.isEmpty()) {
                return binarySearchDynamicCollision(ctd1, ctds, proposedMovement, distanceToNoCollision, halfway, halfwayCollisions, resolution);
            } else {
                return binarySearchDynamicCollision(ctd1, ctds, proposedMovement, halfway, distanceToCollision, collisions, resolution);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}