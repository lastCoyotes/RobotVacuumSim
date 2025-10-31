package robotvacuum.collision;

import robotvacuum.utility.MathHelper;

public class PositionWithRotation {
    private final Position pos;
    private final double rot;

    public PositionWithRotation(Position pos, double rot, MathHelper mathHelper) {
        this.pos = pos;
        this.rot = mathHelper.normalizeAngle(rot);
    }

    public PositionWithRotation(Position pos, double rot) {
        this(pos, rot, new MathHelper());
    }

    public Position getPos() {
        return pos;
    }

    public double getRot() {
        return rot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionWithRotation that = (PositionWithRotation) o;

        return pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
        return pos.hashCode();
    }

    public boolean isCloseTo(PositionWithRotation positionWithRotation) {
        return this.getPos().isCloseTo(positionWithRotation.getPos());
    }
}
