package com.rs.battleship.gui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.*;

import com.rs.battleship.res.ResourceLoader;


public class Dot
{	
	private int x, y;
	
	private float opacity = 1;
	
	private BufferedImage image;
	
	private Radar radar;
	
	public Dot(int x, int y, Radar r)
	{	
		setImage(ResourceLoader.DOT_RED);
		
		radar = r;
		this.x = x - image.getWidth()/2;
		this.y = y - image.getHeight()/2;
	}
	
	public Dot(DotCoords dc, Radar r)
	{
		this(dc.x, dc.y, r);
		
		setImage(ResourceLoader.DOT_BLUE);
	}
	
	/**
	 * Sets image of the Dot
	 * @param img
	 */
    public void setImage(Image img)
    {
    	image = (BufferedImage)img;
    }
    
    /**
     * Draws the dot, updates opacity
     * @param g
     */
    public void draw(Graphics g)
    {
    	Graphics2D g2d = (Graphics2D)g;
    	
    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(image, x, y, null);
        
        if(isIntersectedByRadar())
        {
        	opacity = 1;
        }
        
        opacity -= .008;
        
        opacity = opacity < 0 ? 0 : opacity;
    }
    
    /**
     * Returns true if Radar has passed over Dot
     */
    public boolean isIntersectedByRadar()
    {
    	Line2D line = new Line2D.Double(radar.getXCenter(), radar.getYCenter(), radar.getX1(), radar.getY1());
    	 	
    	return line.intersects(getBoundingBox());
    }

    /**
     * Returns x coordinate
     * @return x
     */
	public int getX() 
	{
		return x;
	}
	
	/**
     * Returns y coordinate
     * @return y
     */
	public int getY() 
	{
		return y;
	}
	
	/**
	 * Returns bounding box of Dot
	 * @return boundingBox
	 */
	public Rectangle getBoundingBox()
	{
		return new Rectangle(x, y, image.getWidth(), image.getHeight());
	}
}