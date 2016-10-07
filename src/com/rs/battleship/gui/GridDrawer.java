/**
 * @author Ryan
 * @date Sep 26, 2013
*/
package com.rs.battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.rs.battleship.game.Block;
import com.rs.battleship.game.Grid;
import com.rs.battleship.game.Ship;
import com.rs.battleship.res.ResourceLoader;


public class GridDrawer 
{
	private ArrayList<Ship> shipsDrawn = new ArrayList<Ship>();
	private ArrayList<Block> blocksWithShips = new ArrayList<Block>();
	
	private ArrayList<Block> hitBlocks = new ArrayList<Block>();
	private ArrayList<Block> missedBlocks = new ArrayList<Block>();
	
	private Grid grid;
	
	public GridDrawer(Grid g)
	{
		grid = g;
	}
	
	/**
	 * Removes all elements to be drawn
	 */
	public void reset()
	{
		shipsDrawn.clear();
		blocksWithShips.clear();
		
		hitBlocks.clear();
		missedBlocks.clear();
	}
	
	/**
	 * Draws Ships on Grid g
	 * @param g
	 */
	public void drawShips(Graphics g)
	{
		for(int i=0; i<shipsDrawn.size(); i++)
		{
			if(shipsDrawn.size() != 0)
			{
				Ship s = shipsDrawn.get(i);
				Block b = blocksWithShips.get(i);
				int x = b.getBoundingBox().x;
				int y = b.getBoundingBox().y;
				
				if(s.getOrientation().equals("horizontal"))
				{
					g.drawImage(ResourceLoader.SHIP_IMAGE, x, y, Block.WIDTH * s.getLength(), Block.WIDTH, null);
				}
				else
				{
					g.drawImage(ResourceLoader.SHIP_VERT_IMAGE, x, y, Block.WIDTH, Block.WIDTH * s.getLength(), null);
				}
			}
		}
	}
	
	/**
	 * Draws hits and misses on Grid g
	 * @param g
	 */
	public void drawHitsMisses(Graphics g)
	{
		for(int i=0; i<hitBlocks.size(); i++)
		{
			Block b = hitBlocks.get(i);
			int x = b.getBoundingBox().x + (Block.WIDTH/2 -10);
			int y = b.getBoundingBox().y + (Block.WIDTH/2 -10);
			
			
			
			g.setColor(Color.RED);
			g.fillOval(x, y, 20, 20);
		}
		
		for(int i=0; i<missedBlocks.size(); i++)
		{
			Block b = missedBlocks.get(i);
			int x = b.getBoundingBox().x + (Block.WIDTH/2 -10);
			int y = b.getBoundingBox().y + (Block.WIDTH/2 -10);
			
			g.setColor(Color.WHITE);
			g.fillOval(x, y, 20, 20);
		}
		
	}
	
	/**
	 * Adds a hit to be drawn on Block b
	 * @param b
	 */
	public void addHit(Block b)
	{
		hitBlocks.add(b);
	}
	
	/**
	 * Adds a miss to be drawn on Block b
	 * @param b
	 */
	public void addMiss(Block b)
	{
		missedBlocks.add(b);
	}
	
	/**
	 * Adds a Ship s to be drawn
	 * @param s
	 */
	public void addShip(Ship s)
	{
		shipsDrawn.add(s);
		Block b = grid.getBlockOn(s.getFirstBlock());
		blocksWithShips.add(b);
	}
	
	/**
	 * Removes Ship s from being drawn
	 * @param s
	 */
	public void removeShip(Ship s)
	{
		String str = s.getFirstBlock();

		for(int i=0; i<blocksWithShips.size(); i++)
		{
			Block bl = blocksWithShips.get(i);
			if(bl.getLoc().equals(str))
			{
				shipsDrawn.remove(i);
				blocksWithShips.remove(i);
				return;
			}
			
		}
	}
	
}
