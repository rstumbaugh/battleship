package com.rs.battleship.game;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ship implements Serializable
{
	private int length = 0,
				health = 0;
	
	private String orientation = "horizontal",
				   firstBlockLocation = "A1";
	
	private boolean isPlaced = false,
					isEnemy = false;
	
	public Ship(int len)
	{
		length = len;
		health = length;
	}
	
	/**
	 * Sets orientation of the Ship
	 * @param newOr
	 */
	public void setOrientation(String newOr)
	{
		if(newOr.equals("horizontal") || newOr.equals("vertical"))
			orientation = newOr;
	}
	
	/**
	 * Returns orientation of the Ship
	 * @return orientation
	 */
	public String getOrientation() 
	{ 
		return orientation; 
	}
	
	/**
	 * Returns length of the Ship
	 * @return length
	 */
	public int getLength() 
	{ 
		return length; 
	}
	
	/**
	 * Returns health of the Ship
	 * @return health
	 */
	public int getHealth() 
	{ 
		return health; 
	}
	
	/**
	 * Resets the health of the Ship
	 */
	public void resetHealth() 
	{ 
		health = length; 
	}

	/**
	 * Sets to true if Ship is an enemy Ship
	 * @param isEn
	 */
	public void setEnemy(boolean isEn) 
	{ 
		isEnemy = isEn; 
	}
	
	/**
	 * Returns true if Ship is an enemy Ship
	 * @return isEnemy
	 */
	public boolean isEnemy() 
	{ 
		return isEnemy; 
	}
	
	/**
	 * Returns true if Ship is placed on a Grid
	 * @return isPlaced
	 */
	public boolean isPlaced() 
	{ 
		return isPlaced; 
	}
	
	/**
	 * Places Ship at location loc
	 * @param loc
	 */
	public void place(String loc)
	{
		firstBlockLocation = loc;
		isPlaced = true;
	}
	
	/**
	 * Returns first Block of the Ship
	 * @return firstBlockLocation
	 */
	public String getFirstBlock() 
	{ 
		return firstBlockLocation; 
	}
	
	/**
	 * Removes Ship from a Grid
	 */
	public void remove()
	{
		isPlaced = false;
	}
	
	/**
	 * Returns true if Ship can fit at location
	 * loc on Grid g
	 * @param g
	 * @param loc
	 * @return canFit
	 */
	public boolean canFitAt(Grid g, String loc)
	{
		boolean canFit = true;
		
		if(Grid.isValidLocation(loc))
		{
			int row = Grid.getRow(loc);
			int col = Grid.getCol(loc);
	
			for(int i=0; i<length; i++)
			{
				String block = orientation.equals("horizontal") ? Grid.locationAt(row, col + i) : Grid.locationAt(row + i, col);
				
				if(!Grid.isValidLocation(block) || !g.getBlockOn(block).isOpen())
				{
					canFit = false;
				}
			}
		}
		else
		{
			canFit = false;
		}
		
		return canFit;
	}
	
	/**
	 * Returns true if Ship is placed on location
	 * loc on Grid g
	 * @param g
	 * @param loc
	 * @return isOn
	 */
	public boolean isOn(Grid g, String loc)
	{
		boolean isOn = false;
		
		if(Grid.isValidLocation(loc))
		{
			int row = Grid.getRow(firstBlockLocation);
			int col = Grid.getCol(firstBlockLocation);
			
			for(int i=0; i<length; i++)
			{
				String block = orientation.equals("horizontal") ? Grid.locationAt(row, col + i) : Grid.locationAt(row + i, col);

				if(Grid.isValidLocation(block) && block.equals(loc))
				{
					isOn = true;
				}
			}
		}
		return isOn;
	}
	
	/**
	 * Hits the Ship, subtracts 1 from health
	 */
	public void hit()
	{
		health --;
	}
	
	/**
	 * Returns true if health == 0
	 * @return isDestroyed
	 */
	public boolean isDestroyed()
	{
		return health == 0;
	}
	
	/**
	 * Converts Ship to String
	 */
	public String toString()
	{
		String st = "";
		
		switch(length)
		{
			case 2:
				st = "patrol";
				break;
			case 3:
				st = "submarine";
				break;
			case 4:
				st = "battleship";
				break;
			case 5:
				st = "carrier";
				break;
		}
		
		return st;
	}
	
	
}
