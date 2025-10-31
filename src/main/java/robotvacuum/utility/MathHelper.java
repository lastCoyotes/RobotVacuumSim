package robotvacuum.utility;

import robotvacuum.collision.CollisionRectangle;
import robotvacuum.collision.CollisionTestData;
import robotvacuum.collision.Position;

import java.io.Serializable;

public class MathHelper implements Serializable {

    private final double TWO_PI = 2 * Math.PI;

    public double normalizeAngle(double angle) {
        return angle - TWO_PI * Math.floor((angle + Math.PI) / TWO_PI);
    }

    public CollisionTestData<CollisionRectangle> changeCenterCoordinatesToTopLeft(CollisionTestData<CollisionRectangle> ctd) {
        final Position pos = ctd.getPos();
        final double width = ctd.getcShape().getWidth();
        final double height = ctd.getcShape().getHeight();

        final Position newPosition = pos.offsetPositionCartesian(new Position(-width/2, -height/2));

        return new CollisionTestData<>(newPosition, ctd.getcShape());
    }

    public CollisionTestData<CollisionRectangle> changeTopLeftCoordinatesToCenter(CollisionTestData<CollisionRectangle> ctd) {
        final Position pos = ctd.getPos();
        final double width = ctd.getcShape().getWidth();
        final double height = ctd.getcShape().getHeight();

        final Position newPosition = pos.offsetPositionCartesian(new Position(width/2, height/2));

        return new CollisionTestData<>(newPosition, ctd.getcShape());
    }
}
