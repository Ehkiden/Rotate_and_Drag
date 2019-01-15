/*
Project:    Exercise 03
File:       Part2Panel.java
Purpose:    Part2Panel.java contains the functions needed to allow for the user to click and drag one of the vertices
            and redraw accordingly
Class:      CS 335
Author:     Jared Rigdon
Date:       10/05/2018
Purpose:    Draws a 5 pointed star. Allows the user to "grab" 1 of the 5 vertices of the polygon and drag them to their
            desired location.

References: Based some of the code from LineMidpoint and SliderDemo provided by Dr. Seales on Canvas
 */

import java.awt.*;
import javax.swing.*;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Part2Panel extends JPanel{
    // polygons for drawing
    private Polygon poly;   //main polygon
    private Polygon poly0;
    private Polygon poly1;
    private Polygon poly2;
    private Polygon poly3;
    private Polygon poly4;

    //points to hold the x and y of the box that is being moved
    private Point bottom= new Point(250,350);   //  0
    private Point bottomR=new Point(345,281);   //  1
    private Point bottomL=new Point(155,281);   //  4
    private Point topR=new Point(309,169);      //  2
    private Point topL=new Point(191,169);      //  3

    //constructor to draw the original polygon
    public Part2Panel(Polygon poly){
        this.poly = poly;
        setBackground (Color.BLACK);
    }

    // this is the polygon that triggers the dragging / rubber-banding
    public int clickInPolyVert(Point click){
        //return an int to be passed back to determine which polygon what clicked on
        //return 5 to indicate that it was not in any polygon
        if(poly0.contains(click)){return 0;}
        else if(poly1.contains(click)){return 1;}
        else if(poly2.contains(click)){return 2;}
        else if(poly3.contains(click)){return 3;}
        else if(poly4.contains(click)){return 4;}
        else{return 5;}
    }

    //get the new polygon
    public void setPoly(Polygon poly){this.poly = poly;}

    //sets up the vertex that is being dragged
    public void set_smallXY(int x, int y, int curr_poly){
        if (curr_poly==0){bottom=new Point(x,y);}
        else if (curr_poly==1){bottomR=new Point(x,y);}
        else if (curr_poly==2){topR=new Point(x,y);}
        else if (curr_poly==3){topL=new Point(x,y);}
        else if (curr_poly==4){bottomL=new Point(x,y);}
    }

    //where the drawing happens
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        //get the proper coordinates to form a star based on the current poly
        for (int i=0; i<poly.npoints;i++){
            int x1,x2,y1,y2;
            x1=poly.xpoints[i];
            y1=poly.ypoints[i];
            if(i==3) {
                x2 = poly.xpoints[0];
                y2 = poly.ypoints[0];
            }
            else if(i==4) {
                x2 = poly.xpoints[1];
                y2 = poly.ypoints[1];
            }
            else{
                x2=poly.xpoints[i+2];
                y2=poly.ypoints[i+2];
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(x1,y1,x2,y2);
        }
        g2d.dispose();

        // draw the small poly, which is the yellow "handle" on the drag point
        draw_small_poly(g2d, bottom,0);
        draw_small_poly(g2d, bottomR,1);
        draw_small_poly(g2d, bottomL,4);
        draw_small_poly(g2d, topR,2);
        draw_small_poly(g2d, topL,3);
    }


    public void draw_small_poly (Graphics2D g, Point curr_poly, int poly_int) {
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(1));

        int x2[] = {curr_poly.x-5,curr_poly.x+5,curr_poly.x+5,curr_poly.x-5};
        int y2[] = {curr_poly.y-5,curr_poly.y-5,curr_poly.y+5,curr_poly.y+5};
        if (poly_int==0){
            poly0  = new Polygon(x2,y2,4);
            g.drawPolygon(poly0);
        }
        else if (poly_int==1){
            poly1  = new Polygon(x2,y2,4);
            g.drawPolygon(poly1);
        }
        else if (poly_int==2){
            poly2  = new Polygon(x2,y2,4);
            g.drawPolygon(poly2);
        }
        else if (poly_int==3){
            poly3 = new Polygon(x2,y2,4);
            g.drawPolygon(poly3);
        }
        else if (poly_int==4){
            poly4  = new Polygon(x2,y2,4);
            g.drawPolygon(poly4);
        }
    }
}