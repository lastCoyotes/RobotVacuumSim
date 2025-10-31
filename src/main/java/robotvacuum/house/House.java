package robotvacuum.house;

import robotvacuum.collision.CollisionRectangle;
import robotvacuum.collision.Position;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tristan Bolern Bolern Bolern Boler  
 */

public class House implements Serializable {
    //TODO: When doors are added to the Room class, addRoom will need to be updated
    //to let the user specify door location.

    //constants
    //All constants are in meters or sq meters
    //TODO: move to enum
    public static final double WALL_THICKNESS = 0.3;               //~1 ft
    private static final double BASE_WALL_THICKNESS = 0.6;         //~2 ft
    private static final double MIN_BASE_ROOM_SIZE = 18.58;        //~200 sq ft
    private static final double MAX_BASE_ROOM_SIZE = 743.22;       //~8000 sq ft
    private static final double DEFAULT_BASE_ROOM_WIDTH = 15.24;   //~50 ft
    private static final double DEFAULT_BASE_ROOM_HEIGHT = 15.24;  //~50 ft
    private static final double MIN_ROOM_SIZE = 0.37;              //~4 sq ft
    private static final double BASE_ROOM_X = 0;                
    private static final double BASE_ROOM_Y = 0;                

    //variables
    private FlooringType floorCovering;
    private final Map<Position, Room> rooms;
    double houseWidth, houseHeight;

    //old constructor
    public House(Map<Position, Room> rooms, FlooringType ft) {
        this.rooms = new HashMap<>(rooms);
        floorCovering = ft;
    }

    //new constructor
    public House(double baseRoomWidth, double baseRoomHeight, FlooringType ft) {
        this.rooms = new HashMap<>();
        floorCovering = ft;
        if (baseRoomWidth * baseRoomHeight < MIN_BASE_ROOM_SIZE) {
            System.out.println("Given base room too small, using default parameters instead.");
            baseRoomWidth = DEFAULT_BASE_ROOM_WIDTH;
            baseRoomHeight = DEFAULT_BASE_ROOM_HEIGHT;
        } else if (baseRoomWidth * baseRoomHeight > MAX_BASE_ROOM_SIZE) {
            System.out.println("Given base room too large, using default parameters instead.");
            baseRoomWidth = DEFAULT_BASE_ROOM_WIDTH;
            baseRoomHeight = DEFAULT_BASE_ROOM_HEIGHT;
        }
        
        /*Walls occupying same space
        Room newRoom = new Room(
                Map.of(new Position(BASE_ROOM_X, BASE_ROOM_Y), new Wall(new Position(BASE_ROOM_X, BASE_ROOM_Y), new CollisionRectangle(BASE_WALL_THICKNESS, baseRoomHeight)),  //left wall
                        new Position(BASE_ROOM_X, BASE_ROOM_Y), new Wall(new Position(BASE_ROOM_X, BASE_ROOM_Y), new CollisionRectangle(baseRoomWidth, BASE_WALL_THICKNESS)),   //top wall
                        new Position((BASE_ROOM_X + baseRoomWidth - BASE_WALL_THICKNESS), BASE_ROOM_Y), new Wall(new Position((BASE_ROOM_X + baseRoomWidth - BASE_WALL_THICKNESS), BASE_ROOM_Y), new CollisionRectangle(BASE_WALL_THICKNESS, baseRoomHeight)),    //right wall
                        new Position(BASE_ROOM_X, (BASE_ROOM_Y + baseRoomHeight - BASE_WALL_THICKNESS)), new Wall(new Position(BASE_ROOM_X, (BASE_ROOM_Y + baseRoomHeight - BASE_WALL_THICKNESS)), new CollisionRectangle(baseRoomWidth, BASE_WALL_THICKNESS))));  //bottom wall

        */
        //Walls not occupying same space
        Room newRoom = new Room(//wallMap);
                Map.of(new Position(BASE_ROOM_X, BASE_ROOM_Y), new Wall(new CollisionRectangle(BASE_WALL_THICKNESS, (baseRoomHeight - BASE_WALL_THICKNESS))),  //left wall
                        new Position((BASE_ROOM_X + BASE_WALL_THICKNESS), BASE_ROOM_Y), new Wall(new CollisionRectangle((baseRoomWidth - BASE_WALL_THICKNESS), BASE_WALL_THICKNESS)),   //top wall
                        new Position((BASE_ROOM_X + baseRoomWidth - BASE_WALL_THICKNESS), (BASE_ROOM_Y + BASE_WALL_THICKNESS)), new Wall(new CollisionRectangle(BASE_WALL_THICKNESS, (baseRoomHeight - BASE_WALL_THICKNESS))),    //right wall
                        new Position(BASE_ROOM_X, (BASE_ROOM_Y + baseRoomHeight - BASE_WALL_THICKNESS)), new Wall(new CollisionRectangle((baseRoomWidth - BASE_WALL_THICKNESS), BASE_WALL_THICKNESS))));  //bottom wall
        newRoom.setIsBaseRoom(true);
        rooms.put(new Position(BASE_ROOM_X, BASE_ROOM_Y), newRoom);
        houseWidth = baseRoomWidth;
        houseHeight = baseRoomHeight;
    }

    /**
     * Adds a new room to the house.
     * Fails if the room is smaller than 4 sq ft,
     * if the room is outside the base room of the house,
     * if the room intersects another room without being entirely inside it,
     * or if the room's walls intersect furniture.
     *
     * @param pos
     * @param room
     */
    public void addRoom(Position pos, Room room) {
        //check eligibility
        if (room.getSize() < MIN_ROOM_SIZE) {
            System.out.println("Invalid size: room cannot be smaller than 4 sq ft.");
            return;
        }

        if (pos.getX() < BASE_ROOM_X || pos.getY() < BASE_ROOM_Y
                || pos.getX() > (BASE_ROOM_X + houseWidth)
                || pos.getY() > (BASE_ROOM_Y + houseHeight)) {
            throw new IllegalArgumentException("Invalid location: out of bounds.");
        }
        //if room intersects another room without being entirely inside it
        //if room's walls intersect furniture

        //add room to house
        rooms.put(pos, room);
    }

    /**
     * Removes a room from the house.
     *
     * @param pos   the position of the room
     */
    public void removeRoom(Position pos) {
        for (Position p : rooms.keySet()) {
            if (p.equals(pos)) {
                if (rooms.get(p).getIsBaseRoom()) {
                    System.out.println("Can't remove base room.");
                    return;
                } else {
                    rooms.remove(p);
                    System.out.println("Room removed:" + p);
                    return;
                }
            }
        }
        System.out.println("Room not found.");
    }

    /**
     * Changes the floor covering of the house.
     *
     * @param ft    the flooring covering type
     */
    public void setFloorCovering(FlooringType ft) {
        floorCovering = ft;
        System.out.println("House floor changed to " + floorCovering);
    }

    /**
     * Returns the floor covering used by the house.
     *
     * @return  the floor covering type
     */
    public FlooringType getFloorCovering() {
        return floorCovering;
    }

    /**
     * Returns the room with the given position.
     *
     * @param pos   the position of the room
     * @return      the room at the given position
     */
    public Room getRoom(Position pos) {
        for (Position p : rooms.keySet()) {
            if (p.equals(pos)) {
                return rooms.get(p);
            }
        }

        return null;
    }

    /**
     * Returns every room in the house.
     *
     * @return  a list of every room
     */
    public Map<Position, Room> getRooms() {
        return Collections.unmodifiableMap(rooms);
    }
    
    public double getHouseWidth() {
        return houseWidth;
    }
    
    public double getHouseHeight() {
        return houseHeight;
    }
}
