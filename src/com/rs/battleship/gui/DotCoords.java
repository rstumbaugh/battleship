package com.rs.battleship.gui;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DotCoords implements Serializable
{

	public int x, y;
	
	/**
	 * Used to receive Dots over Socket
	 * @param x
	 * @param y
	 */
	public DotCoords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Used to send Dots over Socket
	 * @param d
	 */
	public DotCoords(Dot d)
	{
		x = d.getX();
		y = d.getY();
	}

	/**
	 * Converts DotCoords to String
	 */
	public String toString()
	{
		return "X: " + x + " Y: " + y;
	}
	
}
