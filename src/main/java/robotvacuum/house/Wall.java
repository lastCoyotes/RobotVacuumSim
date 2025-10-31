package robotvacuum.house;

import robotvacuum.collision.CollisionRectangle;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Tristan Boler
 */
public class Wall implements Serializable {

    private final CollisionRectangle cRect;

    public Wall(CollisionRectangle cRect) {
        this.cRect = cRect;
    }

    public Wall(Wall wall) {
        this(wall.getcRect());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.cRect);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Wall other = (Wall) obj;
        return Objects.equals(this.cRect, other.cRect);
    }

    /**
     * @return the cRect
     */
    public CollisionRectangle getcRect() {
        return cRect;
    }

}



//package robotvacuum.house;
//
//import robotvacuum.collision.Position;
//
//import java.util.Objects;
//import robotvacuum.collision.CollisionRectangle;
//import java.io.Serializable;
//import java.util.Map;
//import java.util.HashMap;
//
///**
// * @author Tristan Boler
// */
//public class Wall implements Serializable {
//    private final Map<Position, CollisionRectangle> cRects;
//    Position wallPosition;
//    double wallWidth, wallHeight;
//
//    //Wall without door
//    public Wall(Position pos, CollisionRectangle cRect) {
//        wallPosition = pos;
//        wallWidth = cRect.getWidth();
//        wallHeight = cRect.getHeight();
//        cRects = new HashMap<>();
//        cRects.put(pos, cRect);
//    }
//    
//    //Wall with door
//    public Wall(Map<Position, CollisionRectangle> cRects) {
//        this.cRects = cRects;
//    }
//
//    public Wall(Wall wall) {
//        this(wall.getcRects());
//    }
//    
//    /**
//     * Adds a door to a wall.
//     * Removes the present collision rectangle and adds two more 
//     * that have a gap in between them equal to the door width.
//     * 
//     * @param doorWidth the width of the door
//     */
//    public void addDoor(double doorWidth) {
//        if (cRects.size() == 1) {
//            for (Position p : cRects.keySet()) {
//                if (p.equals(wallPosition)) {
//                    if (cRects.get(p).getWidth() > cRects.get(p).getHeight()) { //If cRect is wider than tall, it's horizontal
//                            if (doorWidth > cRects.get(p).getWidth()) {
//                                System.out.println("Error: door too wide.");
//                                return;
//                            }
//                        cRects.remove(p);
//                        cRects.put(p, new CollisionRectangle((wallWidth - doorWidth)/2, wallHeight));
//                        Position newPos = new Position((wallPosition.getX() + ((wallWidth - doorWidth)/2) + doorWidth), wallPosition.getY());
//                        cRects.put(newPos, new CollisionRectangle((wallWidth - doorWidth)/2, wallHeight));
//                    }
//                    else {      //else it's vertical
//                        if (doorWidth > cRects.get(p).getHeight()) {
//                                System.out.println("Error: door too wide.");
//                                return;
//                            }
//                        cRects.remove(p);
//                        cRects.put(p, new CollisionRectangle(wallWidth, (wallHeight - doorWidth)/2));
//                        Position newPos = new Position(wallPosition.getX(), (wallPosition.getY() + ((wallHeight - doorWidth)/2) + doorWidth));
//                        cRects.put(newPos, new CollisionRectangle(wallWidth, (wallHeight - doorWidth)/2));
//                    }
//                }
//                else {
//                    System.out.println("Error: only cRect somehow not at correct position.");
//                }
//            }
//        }
//        else {
//            System.out.println("This wall already has a door.");
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 73 * hash + Objects.hashCode(this.cRects);
//        hash = 73 * hash + Objects.hashCode(this.wallPosition);
//        hash = 73 * hash + (int) (Double.doubleToLongBits(this.wallWidth) ^ (Double.doubleToLongBits(this.wallWidth) >>> 32));
//        hash = 73 * hash + (int) (Double.doubleToLongBits(this.wallHeight) ^ (Double.doubleToLongBits(this.wallHeight) >>> 32));
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Wall other = (Wall) obj;
//        if (Double.doubleToLongBits(this.wallWidth) != Double.doubleToLongBits(other.wallWidth)) {
//            return false;
//        }
//        if (Double.doubleToLongBits(this.wallHeight) != Double.doubleToLongBits(other.wallHeight)) {
//            return false;
//        }
//        if (!Objects.equals(this.cRects, other.cRects)) {
//            return false;
//        }
//        if (!Objects.equals(this.wallPosition, other.wallPosition)) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Returns every collision rectangle in the wall.
//     * 
//     * @return  the set of cRects
//     */
//    public Map<Position, CollisionRectangle> getcRects() {
//        return cRects;
//    }
//    
//    /**
//     * Returns the collision rectangle at the given position.
//     * 
//     * @param pos   the position of the cRect
//     * @return      the cRect at the given position
//     */
//    public CollisionRectangle getcRect(Position pos) {
//        for (Position p : cRects.keySet()) {
//            if (p.equals(pos)) {
//                return cRects.get(p);
//            }
//        }
//
//        return null;
//    }
//    
//    /**
//     * Returns the position of the top left corner of the wall.
//     * 
//     * @return  the position of the wall
//     */
//    public Position getWallPos() {
//        return wallPosition;
//    }
//    
//    /**
//     * Returns the total width of the wall.
//     * 
//     * @return  the width of the wall
//     */
//    public double getWallWidth() {
//        return wallWidth;
//    }
//    
//    /**
//     * Returns the total height of the wall.
//     * 
//     * @return  the height of the wall
//     */
//    public double getWallHeight() {
//        return wallHeight;
//    }
//}
