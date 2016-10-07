package com.rs.battleship.game;

import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rs.battleship.res.ResourceLoader;


@SuppressWarnings("serial")
public class Block extends JPanel 
{
	public static final int WIDTH = Grid.WIDTH / 11 - 2;
	
	private enum State  { NEUTRAL, HIT, MISS };
	State blockState = State.NEUTRAL;
	
	private String location;
	private boolean isOpen = true;

	private JLabel bg = new JLabel(),
				   locLabel = new JLabel();
	
	private Rectangle boundingBox;
	
	public Block(String loc)
	{
		setLayout(null);
		
		location = loc;
		locLabel.setText(location);
		
		bg.setIcon(new ImageIcon(ResourceLoader.WATER_IMAGE));
		
		locLabel.setBounds(10, 10, 30, 15);
		bg.setBounds(0, 0, WIDTH, WIDTH);

		//add location label and background
		if(Grid.isValidLocation(location))
		{
			this.add(bg);
			bg.add(locLabel);
		}
		else
		{
			this.add(locLabel);
		}
	}
	
	/**
	 * Places a ship on the block
	 * @param isEnemyShip
	 */
	public void placeShip(boolean isEnemyShip)
	{
		isOpen = false;
	}
	
	/**
	 * Removes a ship from the block
	 */
	public void removeShip()
	{
		isOpen = true;
	}
	
	/**
	 * Sets the bounds for the Block
	 * @param r
	 */
	public void setBoundingBox(Rectangle r) 
	{ 
		boundingBox = r; 
	}
	
	/**
	 * Returns the bounds for the Block
	 * @return Rectangle boundingBox
	 */
	public Rectangle getBoundingBox() 
	{ 
		return boundingBox; 
	}
	
	/**
	 * Sets Block's state to HIT
	 */
	public void hit() 
	{ 
		blockState = State.HIT;
	}
	
	/**
	 * Returns true if Block is hit
	 * @return state == HIT
	 */
	public boolean isHit() 
	{ 
		return blockState == State.HIT; 
	}
	
	/**
	 * Sets Block's state to MISS
	 */
	public void miss() 
	{ 
		blockState = State.MISS;
	}
	
	/**
	 * Returns true if Block is missed
	 * @return state == MISS
	 */
	public boolean isMissed()
	{
		return blockState == State.MISS;
	}
	
	/**
	 * Returns location of the Block
	 * @return location
	 */
	public String getLoc()
	{ 
		return location; 
	}
	
	/**
	 * Returns true if unoccupied by a ship
	 * @return isOpen
	 */
	public boolean isOpen() 
	{ 
		return isOpen; 
	}
	
	/**
	 * Converts Block to string
	 * @return location + isOpen
	 */
	public String toString()
	{
		return "location="+location+" open="+isOpen;
	}
}
