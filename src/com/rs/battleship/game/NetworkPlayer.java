package com.rs.battleship.game;

import java.io.Serializable;

import com.rs.battleship.game.Player;

@SuppressWarnings("serial")
public class NetworkPlayer extends Player implements Serializable
{
	private Grid grid;
	private boolean isFiring;
	private int index = -1;
	private String status;

	private String ip;
	
	public void setIP(String ip)
	{
		this.ip = ip;
	}
	public String getIP()
	{
		return ip;
	}
	
	public Grid getGrid() 
	{
		return grid;
	}
	public void setGrid(Grid grid) 
	{
		this.grid = grid;
	}
	
	public boolean isFiring()
	{
		return isFiring;
	}
	public void setFiring(boolean isFiring)
	{
		this.isFiring = isFiring;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	public int getIndex()
	{
		return index;
	}
	
	public String getStatus() 
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	
	public String toString()
	{
		return "Player "+(index+1);
	}
	
	
}
