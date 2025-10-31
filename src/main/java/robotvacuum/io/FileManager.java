package robotvacuum.io;

import robotvacuum.house.House;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * OUTDATED - DO NOT USE
 *
 * @author Tristan Boler  
 */
public class FileManager {
    //TODO: When doors are added to the Room class, readHouseFromFile and WriteHouseToFile
    //will need to be updated to include door location in the file.
    //Switching from txt files to JSON would also be preferable.

//    /**
//     * Reads a house from a file in the savedHouses folder.
//     * File format:
//     * First line is three numbers: the width, the height,
//     * and the floor covering's id (1 = hard, 2 = loop pile, 3 = cut pile, 4 = frieze cut pile).
//     * Following lines start with either an 'r' for room
//     * or an 'f' for furniture.
//     * Rooms have four numbers: x, y, width, and height.
//     * Furniture has five numbers: type, x, y, width, and height.
//     * (Furniture types: (chair (1), table (2), or chest (3 or other)).
//     *
//     * @param houseName The name of the house, used as the file name.
//     * @return The data for the house retrieved from the file.
//     */
//    public House readHouseFromFile(String houseName) {
//        House h = new House();
//        Scanner s = null;
//
//        try {
//            s = new Scanner(new BufferedReader(new FileReader("savedHouses/" + houseName + ".txt")));
//
//            h.setWidth(s.nextInt());
//            h.setHeight(s.nextInt());
//            h.setFloorCovering(s.nextInt());
//
//            while (s.hasNext()) {
//                String next = s.next();
//                if ("r".equals(next)) {
//                    h.addRoom(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
//                }
//                else if ("f".equals(next)) {
//                    h.addFurniture(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
//                }
//            }
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        } finally {
//            if (s != null) {
//                s.close();
//            }
//        }
//
//        return h;
//    }

    /**
     * Writes a house's data to a file in the savedHouses folder.
     * File format:
     * First line is three numbers: the width, the height,
     * and the floor covering's id (1 = hard, 2 = loop pile, 3 = cut pile, 4 = frieze cut pile).
     * Following lines start with either an 'r' for room
     * or an 'f' for furniture.
     * Rooms have four numbers: x, y, width, and height.
     * Furniture has five numbers: type, x, y, width, and height.
     * (Furniture types: (chair (1), table (2), or chest (3 or other)).
     *
     * @param h         The house to write to the file.
     * @param houseName The name of the house, used as the file name.
     */
    public void writeHouseToFile(House h, String houseName) {

        try (PrintWriter p = new PrintWriter(new FileWriter("savedHouses/" + houseName + ".txt"))) {

//            p.println((int)h.getWidth() + " " + (int)h.getHeight() + " " + h.getFloorCovering());
//            for (Room r : h.getAllRooms().values()) {
//                p.println("r " + (int)r.getX() + " " + (int)r.getY() + " " + (int)r.getWidth() + " " + (int)r.getHeight());
//            }
//            for (Furniture f : h.getAllFurniture()) {
//                if ("robotvacuum.house.furniture.Chair".equals(f.getClass().getName())) {
//                    p.println("f 1 " + (int)f.getX() + " " + (int)f.getY() + " " + (int)f.getWidth() + " " + (int)f.getHeight());
//                }
//                else if ("robotvacuum.house.furniture.Table".equals(f.getClass().getName())) {
//                    p.println("f 2 " + (int)f.getX() + " " + (int)f.getY() + " " + (int)f.getWidth() + " " + (int)f.getHeight());
//                }
//                else {
//                    p.println("f 3 " + (int)f.getX() + " " + (int)f.getY() + " " + (int)f.getWidth() + " " + (int)f.getHeight());
//                }
//            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
