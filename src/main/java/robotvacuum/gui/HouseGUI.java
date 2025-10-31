package robotvacuum.gui;

import java.awt.*;
import java.awt.event.*;
//import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tristan Boler
 */
public class HouseGUI extends Frame {
    public static final int ROW=700;
    public static final int COL=900;
    public static final int BLOCK_SIZE=1;
    public static final int MIN_X = 20;
    public static final int MIN_Y = 40;
    //private final boolean flag=true;
    
    List<Rectangle> wallRects, chestRects, tableLegs;
    Map<Rectangle, Double> cleanSpots;
    int floorCode, houseWidth, houseHeight;
    Rectangle vacuum;
    
    public void launch(List<Rectangle> wallRects, List<Rectangle> chestRects, 
            List<Rectangle> tableLegs, int floorCode, int houseWidth, int houseHeight)
    {
        this.wallRects = wallRects;
        this.chestRects = chestRects;
        this.tableLegs = tableLegs;
        this.floorCode = floorCode;
        this.houseWidth = houseWidth;
        this.houseHeight = houseHeight;
        cleanSpots = null;
        vacuum = null;
        
        setSize(BLOCK_SIZE*COL, BLOCK_SIZE*ROW);
        setLocation(450,0);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
        
        // new Thread(new PaintThread()).start();

        //then clean finish, call cleanResult interface class show result and end program.
    }
    
    public void redo(List<Rectangle> wallRects, List<Rectangle> chestRects, List<Rectangle> tableLegs) {
        this.wallRects = wallRects;
        this.chestRects = chestRects;
        this.tableLegs = tableLegs;
        cleanSpots = null;
        repaint();
    }
    
    public void redoAll(List<Rectangle> wallRects, List<Rectangle> chestRects, 
            List<Rectangle> tableLegs, int floorCode, int houseWidth, int houseHeight) 
    {
        this.wallRects = wallRects;
        this.chestRects = chestRects;
        this.tableLegs = tableLegs;
        this.floorCode = floorCode;
        this.houseWidth = houseWidth;
        this.houseHeight = houseHeight;
        cleanSpots = null;
        repaint();
    }
    
    public void redoVacuum(Rectangle vacuum) {
        this.vacuum = vacuum;
        //cleanSpots = null;
        if (vacuum != null) {
            repaint((vacuum.x*BLOCK_SIZE)+MIN_X-5, (vacuum.y*BLOCK_SIZE)+MIN_Y-5, vacuum.width*BLOCK_SIZE+10, vacuum.height*BLOCK_SIZE+10);
        }
    }
    
    public void paintCleanSpots(Map<Rectangle, Double> cleanSpots) {
        this.cleanSpots = cleanSpots;
        repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        //Color c=g.getColor();
        //g.setColor(Color.WHITE);
        //g.fillRect(0,0,BLOCK_SIZE*COL,BLOCK_SIZE*ROW);  //paint house
        
        //set the line color now as orange, so we can clearly see the lines
        // after we finish to build the robot, we can set the line as the house background color
        /*g.setColor(Color.LIGHT_GRAY);
        for(int i=1; i<ROW;i++)
        {
            g.drawLine(0,BLOCK_SIZE*i,COL*BLOCK_SIZE,i*BLOCK_SIZE);
        }
        for(int i=1; i<COL;i++)
        {
            g.drawLine(BLOCK_SIZE*i,0,i*BLOCK_SIZE,ROW*BLOCK_SIZE);
        }*/
        
        //floor
        switch (floorCode) {
            case 1:
                //hard
                g.setColor(Color.LIGHT_GRAY);
                for (int i=0; i < houseHeight*BLOCK_SIZE; i=i+10) {
                    g.drawLine(0+MIN_X, BLOCK_SIZE*i+MIN_Y, houseWidth*BLOCK_SIZE+MIN_X, BLOCK_SIZE*i+MIN_Y);
                }   break;
            case 2:
                //loop
                g.setColor(Color.LIGHT_GRAY);
                for (int i=5; i < houseWidth*BLOCK_SIZE-5; i=i+4) {
                    for (int j=5; j < houseHeight*BLOCK_SIZE-5; j=j+8) {
                        g.drawLine(BLOCK_SIZE*i+MIN_X, BLOCK_SIZE*j+MIN_Y, BLOCK_SIZE*i+MIN_X, BLOCK_SIZE*j+4+MIN_Y);
                    }
                }   break;
            case 3:
                //cut
                g.setColor(Color.GRAY);
                for (int i=4; i < houseWidth*BLOCK_SIZE-5; i=i+3) {
                    for (int j=4; j < houseHeight*BLOCK_SIZE-5; j=j+3) {
                        g.fillRect(BLOCK_SIZE*i+MIN_X, BLOCK_SIZE*j+MIN_Y, 1, 1);
                    }
                }   break;
            case 4:
                //frieze
                g.setColor(Color.GRAY);
                for (int i=5; i < houseWidth*BLOCK_SIZE-5; i=i+4) {
                    for (int j=5; j < houseHeight*BLOCK_SIZE-5; j=j+6) {
                        g.drawLine(BLOCK_SIZE*i+MIN_X, BLOCK_SIZE*j+MIN_Y, BLOCK_SIZE*i-3+MIN_X, BLOCK_SIZE*j+3+MIN_Y);
                    }
                }   break;
            default:
                break;
        }
        
        //walls
        g.setColor(Color.BLACK);
        for (Rectangle r : wallRects) {
            g.fillRect((r.x*BLOCK_SIZE)+MIN_X, (r.y*BLOCK_SIZE)+MIN_Y, r.width*BLOCK_SIZE, r.height*BLOCK_SIZE);
        }
        
        //chests
        g.setColor(new Color(100, 50, 45));
        for (Rectangle r : chestRects) {
            g.fillRect((r.x*BLOCK_SIZE)+MIN_X, (r.y*BLOCK_SIZE)+MIN_Y, r.width*BLOCK_SIZE, r.height*BLOCK_SIZE);
        }
        
        //tables
        g.setColor(new Color(125, 65, 50));
        for (Rectangle r : tableLegs) {
            g.fillOval((r.x*BLOCK_SIZE)+MIN_X, (r.y*BLOCK_SIZE)+MIN_Y, r.width*BLOCK_SIZE, r.height*BLOCK_SIZE);
        }
        
        //clean spots
        if (cleanSpots != null) {
            for (Rectangle r : cleanSpots.keySet()) {
                if (cleanSpots.get(r) > 0) {
                    g.setColor(new Color(0, 255, 0, (int)(255*cleanSpots.get(r))));
                    g.fillRect((r.x*BLOCK_SIZE)+MIN_X, (r.y*BLOCK_SIZE)+MIN_Y, r.width*BLOCK_SIZE, r.height*BLOCK_SIZE);
                }
            }
        }
        
        //vacuum
        g.setColor(Color.DARK_GRAY);
        if (vacuum != null) {
            g.fillOval((vacuum.x*BLOCK_SIZE)+MIN_X, (vacuum.y*BLOCK_SIZE)+MIN_Y, vacuum.width*BLOCK_SIZE, vacuum.height*BLOCK_SIZE);
        }
    }

/*
    //class for run the robot and set the running speed
    // wait until we build the robot
    private class PaintThread implements Runnable{
        @Override
        public void run()
        {
            while(flag)
            {
                repaint();
                try{
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
    }*/
}
