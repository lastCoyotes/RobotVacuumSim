package robotvacuum.robot;

/**
 * @author Tristan Boler
 */
public interface VacuumStrategy {
    ProposedMovement vacuum(RobotSimulationState rSimState);
}
