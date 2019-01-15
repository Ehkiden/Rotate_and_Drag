/*
Project:    Exercise 03
File:       Part1.java
Purpose:    Draws a 5 pointed star that rotates a full 360 degrees around the center based on the user click at
            one frame per degree.
            Allows the user to stop the rotation at any point or reset to original position and orientation.
Class:      CS 335
Author:     Jared Rigdon
Date:       10/05/2018

References: Based some of the code from LineMidpoint and SliderDemo provided by Dr. Seales on Canvas
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public class Part1 extends JPanel implements MouseListener, ActionListener{
    //hard code the 5 verticies
    private int x[]={250,345,309,191,155};
    private int y[]={350,281,169,169,281};

    //polygon to hold the current points of the shape
    private Polygon poly;

    //set up the points for the polygon and center of rotation
    private Point[] pt1;
    private Point center;

    //buttons to stop and reset the rotation and the panel to hold them
    private JButton reset, stop;
    private JPanel buttHolder;

    //angle and times for frame per angle
    private double angle;
    private Timer timer;
    private static int runTime;
    private long start, sleepTime;
    private int count=0;
    private boolean stop_flag, reset_flag;

    public Part1(){
        //call the main constructor and the buttons
        super();
        JButton reset = new JButton("Reset");
        JButton stop = new JButton("Stop");
        setBackground(Color.BLACK);

        //have the action listener to just stop the process but not reset until the user clicks again
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop_flag=true;
            }
        });

        //when clicked, raises the reset flag and sets polygon to original image
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset_flag=true;
                //redraws the polygon to the original coords
                pt1=OriginCoord();
                angle=0;
                poly=createPoly(pt1);
                repaint();
            }
        });

        //frame for the buttons
        buttHolder = new JPanel();

        buttHolder.setLayout(new GridLayout(1,2,2,4));
        buttHolder.add(stop);
        buttHolder.add(reset);
        add(buttHolder);

        //set the time to limit at 50 ms rate
        runTime = 50;
        start = 0;
        sleepTime = 0;
        timer = new Timer();
        angle = 0;

        //initializes the array
        pt1=OriginCoord();

        //get the center of rotation with a mouse click
        addMouseListener(this);
    }

    //holds the main loop that calls the rotation until a full rotation is met or stop occcurs
    public void rotateloop(){
        //set up a flag to check for exit statements
        boolean exit_flag=false;
        //start the timer
        start = System.currentTimeMillis();

        //get the new pointer coord using the original coord and the current angle
        rotateStar(OriginCoord(),angle,pt1);

        //assign the new coord to the polygon
        poly = createPoly(pt1);

        //increase the angle by 1 and check for  exit statements
        angle++;
        if(angle>=360 || stop_flag || reset_flag){
            //repaint only if the reset flag was not raised
            if (!reset_flag){
                repaint();
            }
            count=0;
            angle=0;
            stop_flag=false;
            reset_flag=false;
            exit_flag=true;
        }

        //update the image before exiting or continuing the loop

        if(!exit_flag){
            repaint();
            //set up a sleep timer got the program
            sleepTime = runTime -(System.currentTimeMillis()-start);
            if(sleepTime>0)
                timer.schedule(new loop(), sleepTime);
            else {
                //continue with the loop
                rotateloop();
            }
        }

    }

    //uses the original vertices and the current angle to calculate new vertices
    public void rotateStar(Point[] ogPt, double angle, Point[] temp){
        AffineTransform.getRotateInstance(Math.toRadians(angle),center.x,center.y).transform(ogPt,0,temp,0,5);

    }

    //create a temp polygon from the rotated points
    public Polygon createPoly(Point[] polyPoints){
        Polygon tempPoly = new Polygon();

        for (int i=0; i < polyPoints.length; i++){
            tempPoly.addPoint(polyPoints[i].x, polyPoints[i].y);
        }
        return tempPoly;
    }

    //get the click location and limit to one click
    public void mouseClicked(MouseEvent mouse){
        if (count <1){
            count++;
            center=new Point(mouse.getX(),mouse.getY());
            rotateloop();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    //draws the star based on the current polygon coordinates
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        //get the proper coordinates to form a star
        for (int i=0; i<x.length;i++){
            int x1,x2,y1,y2;
            x1=pt1[i].x;
            y1=pt1[i].y;
            if(i==3) {
                x2 = pt1[0].x;
                y2 = pt1[0].y;
            }
            else if(i==4) {
                x2 = pt1[1].x;
                y2 = pt1[1].y;
            }
            else{
                x2=pt1[i+2].x;
                y2=pt1[i+2].y;
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(x1,y1,x2,y2);
        }
        g2d.drawString("Angle: "+angle,10,20);
        g2d.dispose();
    }

    //get the static original coordinates.... just in case
    public Point[] OriginCoord(){
        Point[] originPt=new Point[5];
        originPt[0]=new Point(250,350);
        originPt[1]=new Point(345,281);
        originPt[2]=new Point(309,169);
        originPt[3]=new Point(191,169);
        originPt[4]=new Point(155,281);

        return originPt;
    }

    //loop to make sure it stays at a fixed rate
    class loop extends TimerTask{
        public void run(){
            rotateloop();
        }
    }

    //construct the frame and panels needed
    public static void main(String[] arg){
        JFrame frame = new JFrame("drawStar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.add(new Part1());
        Part1 panel = new Part1();
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
