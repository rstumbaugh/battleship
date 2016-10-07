/**
 * @(#)Radar.java
 *
 *
 * @author 
 * @version 1.00 2012/11/21
 */

package com.rs.battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.battleship.main.Main;


@SuppressWarnings("serial")
public class Radar extends JPanel
	implements ActionListener, MouseListener{
		
	public static final int WIDTH = 250;
	
	private int x = WIDTH,
			    y = WIDTH / 2,
			    degree = 360, 
			    radius = WIDTH / 2 +50;
	
	public int xCenter = WIDTH / 2,
			   yCenter = xCenter;
	
	public ArrayList<Dot> dots = new ArrayList<Dot>();
	
	public Radar() 
    {
    	setBackground(Color.black);
    	
    	Timer clock = new Timer(30, this);
    	clock.start();	
    	
    	addMouseListener(this);
    }
	
	/**
	 * Returns x coordinate
	 * @return x
	 */
	public int getX1() 
	{
		return x;
	}

	/**
	 * Returns y coordinate
	 * @return y
	 */
	public int getY1() 
	{
		return y;
	}

	/**
	 * Returns x coordinate of center
	 * @return xCenter
	 */
	public int getXCenter() 
	{
		return xCenter;
	}

	/**
	 * Returns y coordinate of center
	 * @return yCenter
	 */
	public int getYCenter() 
	{
		return yCenter;
	}

	/**
	 * Adds a new Dot at (x, y)
	 * @param x
	 * @param y
	 */
    public void addDot(int x, int y)
    {
    	Dot d = new Dot(x, y, this);
    	dots.add(d);
    	
    	if(Main.networkGame.isConnected())
    	{
    		sendDotCoords(d);
    	}
    }
    
    /**
     * Sends DotCoords over Socket
     * @param d
     */
    public void sendDotCoords(Dot d)
    {
		Main.networkGame.connection.send(new DotCoords(d));
    }
    
    /**
     * Adds a Dot at DotCoords
     * @param dc
     */
    public void addDot(DotCoords dc)
    {
    	dots.add(new Dot(dc, this));
    }
    
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	
    	g.setColor(Color.green);
    	g.drawOval(2, 2, WIDTH - 5, WIDTH - 5 );
    	g.drawOval(25, 25, WIDTH - 50, WIDTH - 50);
    	g.drawOval(62, 62, WIDTH - 125, WIDTH - 125);
    	g.fillOval(100, 100, WIDTH - 200, WIDTH - 200);
    	
    	g.drawLine(WIDTH / 2, 0, WIDTH / 2, WIDTH);
    	g.drawLine(0, WIDTH / 2, WIDTH, WIDTH / 2);
    	
    	g.drawLine(xCenter, yCenter, x, y);
    	
    	for (int i=0; i<dots.size(); i++)
    	{
    		Dot d = dots.get(i);
    		d.draw(g);
    	}
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	x = xCenter + (int)(radius * Math.cos(degree * Math.PI / 180.0));
    	y = yCenter + (int)(radius * Math.sin(degree * Math.PI / 180.0));

    	degree-=2;
    	    	
    	repaint();
    }
    
    public void mousePressed(MouseEvent e) 
	{
    	addDot(e.getX(), e.getY());
	}
    
    public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}