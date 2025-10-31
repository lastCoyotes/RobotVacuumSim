package robotvacuum.collision;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import robotvacuum.robot.ActualMovement;
import robotvacuum.robot.Movement;
import robotvacuum.robot.MovementLineSegment;
import robotvacuum.robot.ProposedMovement;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CollisionDetectorDynamicTest {

    final CollisionDetector collisionDetector = new CollisionDetector();

    @Test
    void detectDynamicCollisionBetweenCircleAndRectangleLineMovement() {
        CollisionTestData<CollisionCircle> robotTestData = generateMockCollisionCircleTestData(1.0, 1.0, 1.0);
        CollisionTestData<CollisionRectangle> wallTestData = generateMockCollisionRectangleTestData(3.0, 0.0, 2.0, 2.0);
        final ProposedMovement proposedMovement =
                new ProposedMovement(new MovementLineSegment(robotTestData.getPos(), new Position(5.0, 1.0)));

        ActualMovement actualMovement =
                collisionDetector.detectDynamicCollision(robotTestData, wallTestData, proposedMovement);

        assertFalse(actualMovement.getCollisions().isEmpty());
        Optional<Movement> optMovement = actualMovement.getMovement();
        assertTrue(optMovement.isPresent());
        Optional<Collision> optCollision = actualMovement.getCollisions().stream().findFirst();
        assertTrue(optCollision.isPresent());

        Movement movement = optMovement.orElseThrow();
        Collision collision = optCollision.orElseThrow();
        Position stopPos = movement.getStopPosWithRotation().getPos();
        assertEquals(2.0, stopPos.getX(), 0.01);
        assertEquals(1.0, stopPos.getY(), 0.01);
        assertEquals(3.0, collision.getCollisionPosition().getX(), 0.01);
        assertEquals(1.0, collision.getCollisionPosition().getY(), 0.01);
    }

    private CollisionTestData<CollisionRectangle> generateMockCollisionRectangleTestData(double x, double y, double width, double height) {
        CollisionRectangle rectMock = Mockito.mock(CollisionRectangle.class);
        when(rectMock.getHeight()).thenReturn(height);
        when(rectMock.getWidth()).thenReturn(width);
        when(rectMock.getShape()).thenReturn(Shape.RECTANGLE);
        Position pos = new Position(x, y);
        CollisionTestData<CollisionRectangle> ctdMock = Mockito.mock(CollisionTestData.class);
        when(ctdMock.getcShape()).thenReturn(rectMock);
        when(ctdMock.getPos()).thenReturn(pos);
        return ctdMock;
    }

    private CollisionTestData<CollisionCircle> generateMockCollisionCircleTestData(double x, double y, double radius) {
        CollisionCircle circleMock = Mockito.mock(CollisionCircle.class);
        when(circleMock.getRadius()).thenReturn(radius);
        when(circleMock.getShape()).thenReturn(Shape.CIRCLE);
        Position pos = new Position(x, y);
        CollisionTestData<CollisionCircle> ctdMock = Mockito.mock(CollisionTestData.class);
        when(ctdMock.getcShape()).thenReturn(circleMock);
        when(ctdMock.getPos()).thenReturn(pos);
        return ctdMock;
    }
}