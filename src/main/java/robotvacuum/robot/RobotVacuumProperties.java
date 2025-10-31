package robotvacuum.robot;

import robotvacuum.collision.CollisionCircle;
import robotvacuum.house.FlooringType;

import java.util.EnumMap;
import java.util.Map;

import java.io.Serializable;

/**
 * @author Tristan Boler
 */
public class RobotVacuumProperties implements Serializable {
    private final CollisionCircle cCircle;
    private final double vacuumWidth;
    private final Map<FlooringType, Double> vacuumEfficiency;
    private final double whiskersWidth;
    private final Map<FlooringType, Double> whiskersEfficiency;
    private final double speed;
    private final int maxBatteryLife;

    public RobotVacuumProperties(CollisionCircle cCircle,
                                 double vacuumWidth,
                                 Map<FlooringType, Double> vacuumEfficiency,
                                 double whiskersWidth,
                                 Map<FlooringType, Double> whiskersEfficiency,
                                 double speed,
                                 int maxBatteryLife) {
        this.cCircle = new CollisionCircle(cCircle);
        this.vacuumWidth = vacuumWidth;
        this.vacuumEfficiency = new EnumMap<>(vacuumEfficiency);
        this.whiskersWidth = whiskersWidth;
        this.whiskersEfficiency = new EnumMap<>(whiskersEfficiency);
        this.speed = speed;
        this.maxBatteryLife = maxBatteryLife;
    }

    /**
     * @return the cCircle
     */
    public CollisionCircle getcCircle() {
        return cCircle;
    }

    /**
     * @return the vacuumWidth
     */
    public double getVacuumWidth() {
        return vacuumWidth;
    }

    /**
     * @return the vacuumEfficiency
     */
    public EnumMap<FlooringType, Double> getVacuumEfficiency() {
        return new EnumMap<>(vacuumEfficiency);
    }

    /**
     * @return the whiskersWidth
     */
    public double getWhiskersWidth() {
        return whiskersWidth;
    }

    /**
     * @return the whiskersEfficiency
     */
    public EnumMap<FlooringType, Double> getWhiskersEfficiency() {
        return new EnumMap<>(whiskersEfficiency);
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return the maxBatteryLife
     */
    public int getMaxBatteryLife() {
        return maxBatteryLife;
    }

}
