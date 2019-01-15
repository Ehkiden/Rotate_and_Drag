/*
Project:    Exercise 03
File:       Part2.java
Purpose:    Part2.java contains the main constructor that initializes the mouse drag event and frame for the program.
Class:      CS 335
Author:     Jared Rigdon
Date:       10/05/2018
Purpose:    Draws a 5 pointed star. Allows the user to "grab" 1 of the 5 vertices of the polygon and drag them to their
            desired location.

References: Based some of the code from LineMidpoint and SliderDemo provided by Dr. Seales on Canvas
 */

import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Part2 extends JFrame {
    //hard code the 5 vertices
    private int x[]={250,345,309,191,155};
    private int y[]={350,281,169,169,281};

    //set up another set of vertices to be overwritten
    private int x2[]={250,345,309,191,155};
    private int y2[]={350,281,169,169,281};

    //call the polygon obj
    private Polygon poly = new Polygon(x,y,5);
    private Container c;
    private Part2Panel panel;

    //call the boolean to check if the mouse is being dragged
    private boolean isDragging = false;
    //stores the currently dragged vertex
    private int curr_poly;

    public Part2(){
        c = getContentPane();

        //call the panel the obj will be located in
        panel= new Part2Panel(poly);

        //add the mouse listener to verify that a vertex is being clicked
        panel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {isDragging=false;}
            @Override
            public void mousePressed(MouseEvent e) {
                //check that the mouse pressed location is in a valid area
                if(panel.clickInPolyVert(e.getPoint())<5){
                    curr_poly=panel.clickInPolyVert(e.getPoint());
                    isDragging = true;
                }
            }
        });
        //add the event listener for when the mouse is being dragged and update accordingly
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging){
                    //update the array based on the current vertex being dragged
                    x2[curr_poly]=e.getX();
                    y2[curr_poly]=e.getY();

                    //update the current vertex location in the panel
                    panel.set_smallXY(e.getX(), e.getY(), curr_poly);
                    //setup the new polygon and update
                    panel.setPoly(new Polygon(x2,y2,5));
                    panel.repaint();
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });
        c.add(panel, BorderLayout.CENTER);
        setSize(500, 500);
        setVisible(true);
    }

    public static void main(String[] args){
        Part2 poly = new Part2();
        poly.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
