package robotvacuum.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import static java.awt.Font.PLAIN;

/**
 * 
 * @author Tristan Boler
 */
public class StartScreen {
    JFrame frame;
    JLabel label;
    JButton startButton;
    JButton exitButton;
    Font titleFont=new Font("Times New Roman", PLAIN, 35);

    public StartScreen(){
        System.out.println("Welcome to the virtual robot vacuum cleaner!\n" +
                "Press 'Start' button to begin\nPress 'Exit' button to exit.\n ");
//------LABEL--------------------------------------------------------
        //label in frame
        label = new JLabel("Welcome to the virtual robot vacuum cleaner!");
        label.setForeground(Color.white);
        label.setFont(titleFont);
        label.setBounds(60,100,650,150);
        label.setVisible(true);

//------BUTTON------------------------------------------------------
        //start button in frame
        startButton = new JButton("Start");
        startButton.setBounds(200,300,100,100);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getSource()==startButton){
                    System.out.println("> You chose 'start' button.");
                    new HouseEditor().setVisible(true);
                    
                    frame.setVisible(false);
                }
            }
        });
        startButton.setBackground(Color.ORANGE);

        //exit button in frame
        exitButton = new JButton("Exit");
        exitButton.setBounds(400,300,100,100);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getSource()==exitButton){
                    System.out.println("> You chose 'Exit' button. Quitting the program. ");
                    System.exit(0);
                }
            }
        });
        exitButton.setBackground(Color.ORANGE);

//-----FRAME-----------------------------------------------------------
        //window for startScreen
        frame = new JFrame("Virtual Robot Vacuum");
        frame.setSize(800,600);
        frame.setLocation(20,20);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.add(label);
        frame.add(startButton);
        frame.add(exitButton);
        frame.setVisible(true);
    }
}
