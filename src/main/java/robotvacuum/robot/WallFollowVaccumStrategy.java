/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotvacuum.robot;

import java.util.Collection;
import java.util.Random;
import robotvacuum.collision.Collision;
import robotvacuum.collision.Position;
import robotvacuum.utility.MathHelper;

/**
 *
 * @author Tristan Boler
 */
public class WallFollowVaccumStrategy implements VacuumStrategy {
    
    private final double defaultVacuumDistance;
    private double previousDirection = 0.0;
    private Random random;
    private final MathHelper mathHelper;
    private boolean perimeterCompleted = false;
    
    WallFollowVaccumStrategy(long seed, double defaultVacuumDistance) {
        this.defaultVacuumDistance = defaultVacuumDistance;
        this.random = new Random(seed);
        this.mathHelper = new MathHelper();
    }
    
    public WallFollowVaccumStrategy(long seed, double defaultVacuumDistance, double startingDirection) {
        this(seed, defaultVacuumDistance);
        this.previousDirection = startingDirection;
    }
    
    @Override
     public ProposedMovement vacuum(RobotSimulationState rSimState, Collection<Collision> previousCollisions) {
         if (previousCollisions.isEmpty()) {
            return new ProposedMovement(
                    new MovementLineSegment(
                            rSimState.getPosition(),
                            rSimState.getPosition().offsetPositionPolar(previousDirection, defaultVacuumDistance)));
        } else {
             double minPossibleDirection =
                    previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).map(x -> x + Math.PI / 2).max().getAsDouble();

            double maxPossibleDirection =
                    previousCollisions.stream().mapToDouble(Collision::getCollisionDirection).map(x -> x - Math.PI / 2).min().getAsDouble()
                            + 2 * Math.PI;
            
            
            double turningAngle;
            TurnBehavior behavior = TurnBehavior.NOTURN;
            double wallDirection = Math.floor(random.nextDouble() * 4);
            
            if(wallDirection == 0) {
                if(this.previousDirection == 0) { // Facing Top
                    behavior = TurnBehavior.COUNTERCLOCKWISE;
                } else if(this.previousDirection == 1.57079633) { // Facing Right
                    behavior = TurnBehavior.CLOCKWISE;
                }
            } else if(wallDirection == 1) {
                if(this.previousDirection == 0) { // Facing Top
                    behavior = TurnBehavior.CLOCKWISE;
                } else if(this.previousDirection == 4.71238898) { // Facing Left
                    behavior = TurnBehavior.COUNTERCLOCKWISE;
                }
            } else if(wallDirection == 2) {
                if(this.previousDirection == 3.14159265) {
                    behavior = TurnBehavior.CLOCKWISE;
                } else if (this.previousDirection == 1.57079633) {
                    behavior = TurnBehavior.COUNTERCLOCKWISE;
                }
            }  else if(wallDirection == 3) {
                if(this.previousDirection == 3.14159265) {
                    behavior = TurnBehavior.COUNTERCLOCKWISE;
                } else if (this.previousDirection == 4.71238898) {
                    behavior = TurnBehavior.CLOCKWISE;
                }
            }
            
            switch(behavior) {
                case CLOCKWISE:
                    turningAngle = 1.57079633;
                    break;
                case COUNTERCLOCKWISE:
                    turningAngle = -1.57079633;
                    break;
                default:
                    turningAngle = 0.0;
                     break;
            }

            double direction = mathHelper.normalizeAngle((turningAngle * (maxPossibleDirection - minPossibleDirection)) + minPossibleDirection);

            this.previousDirection = direction;

            return new ProposedMovement(
                    new MovementLineSegment(
                            rSimState.getPosition(),
                            rSimState.getPosition().offsetPositionPolar(direction, defaultVacuumDistance)));
        }
     }
}
