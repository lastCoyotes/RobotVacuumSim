package robotvacuum.house.furniture;

import java.util.Collection;

import robotvacuum.collision.CollisionShape;
import robotvacuum.collision.CollisionTestData;

/**
 *
 * @author Tristan Boler  
 */

public interface Furniture {

    Collection<CollisionTestData<? extends CollisionShape>> getCollisionTestData();
}
