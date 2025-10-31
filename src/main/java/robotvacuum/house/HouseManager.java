package robotvacuum.house;

import robotvacuum.house.furniture.*;
import robotvacuum.collision.*;
import robotvacuum.io.*;

import java.util.List;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Tristan Bolern Boler  
 */
public class HouseManager {
    //TODO: move to enum
    public static final double SCALE_FACTOR = 20;
    
    House h;
    final Serializer s;
    
    public HouseManager() {
        h = null;
        s = new Serializer();
    }
    
    public HouseManager(double baseRoomWidth, double baseRoomHeight, FlooringType ft) {
        h = new House(baseRoomWidth, baseRoomHeight, ft);
        s = new Serializer();
    }
    
    public void setHouse(double baseRoomWidth, double baseRoomHeight, FlooringType ft) {
        h = new House(baseRoomWidth, baseRoomHeight, ft);
    }
    
    public void setHouse(House h) {
        this.h = h;
    }
    
    public House getHouse() {
        return h;
    }
    
    public Serializer getSerializer() {
        return s;
    }
    
    public List<Rectangle> getWalls() {
        List<Rectangle> walls = new ArrayList<>();
        Map<Position, Room> rooms = h.getRooms();
        double tempX, tempY, tempWidth, tempHeight;
        //absolute position
//        for (Room r : rooms.values()) {
//            for (Position p : r.getWalls().keySet()) {
//                tempX = p.getX() * SCALE_FACTOR;
//                tempY = p.getY() * SCALE_FACTOR;
//                tempWidth = r.getWalls().get(p).getcRect().getWidth() * SCALE_FACTOR;
//                tempHeight = r.getWalls().get(p).getcRect().getHeight() * SCALE_FACTOR;
//                walls.add(new Rectangle((int)tempX, (int)tempY, (int)tempWidth, (int)tempHeight));
//            }
//        }
        //relative position
        for (Position p1 : rooms.keySet()) {
            Room r = rooms.get(p1);
            for (Position p2 : r.getWalls().keySet()) {
                tempX = (p1.getX() + p2.getX()) * SCALE_FACTOR;
                tempY = (p1.getY() + p2.getY()) * SCALE_FACTOR;
                tempWidth = r.getWalls().get(p2).getcRect().getWidth() * SCALE_FACTOR;
                tempHeight = r.getWalls().get(p2).getcRect().getHeight() * SCALE_FACTOR;
                walls.add(new Rectangle((int)tempX, (int)tempY, (int)tempWidth, (int)tempHeight));
            }
        }
        
        return walls;
    }
    
    public List<Rectangle> getChests() {
        List<Rectangle> chests = new ArrayList<>();
        Map<Position, Room> rooms = h.getRooms();
        double tempX, tempY, tempWidth, tempHeight;
        CollisionShape tempShape;

        for (Room r : rooms.values()) {
            for (Furniture f : r.getAllFurniture()) {
                if (f instanceof Chest) {
                    tempX = ((Chest) f).getcData().getPos().getX() * SCALE_FACTOR;
                    tempY = ((Chest) f).getcData().getPos().getY() * SCALE_FACTOR;
                    tempShape = ((Chest) f).getcData().getcShape();
                    tempWidth = ((CollisionRectangle) tempShape).getWidth() * SCALE_FACTOR;
                    tempHeight = ((CollisionRectangle) tempShape).getHeight() * SCALE_FACTOR;
                    chests.add(new Rectangle((int)tempX, (int)tempY, (int)tempWidth, (int)tempHeight));
                }
            }
        }
        
        return chests;
    }
    
    public List<Rectangle> getTableLegs() {
        List<Rectangle> tableLegs = new ArrayList<>();
        Map<Position, Room> rooms = h.getRooms();
        double tempX, tempY, tempWidth, tempHeight;
        CollisionShape tempShape;
        
        for (Room r : rooms.values()) {
            for (Furniture f : r.getAllFurniture()) {
                if (f instanceof Table) {
                    for (Position p : ((Table) f).getLegs().keySet()) {
                        tempShape = ((Table) f).getLeg(p).getcShape();
                        tempX = (p.getX() - ((CollisionCircle) tempShape).getRadius()) * SCALE_FACTOR;
                        tempY = (p.getY() - ((CollisionCircle) tempShape).getRadius()) * SCALE_FACTOR;
                        tempWidth = ((CollisionCircle) tempShape).getRadius() * 2 * SCALE_FACTOR;
                        tempHeight = ((CollisionCircle) tempShape).getRadius() * 2 * SCALE_FACTOR;
                        tableLegs.add(new Rectangle((int)tempX, (int)tempY, (int)tempWidth, (int)tempHeight));
                    }
                }
            }
        }
        
        return tableLegs;
    }
    
    public int getFloorCode() {
        switch (h.getFloorCovering()) {
            case HARD:
                return 1;
            case LOOP:
                return 2;
            case CUT:
                return 3;
            case FRIEZE:
                return 4;
            default:
                return 1;
        }
    }
    
    public double getHouseWidth() {
        return h.getHouseWidth()*SCALE_FACTOR;
    }
    
    public double getHouseHeight() {
        return h.getHouseHeight()*SCALE_FACTOR;
    }
}
