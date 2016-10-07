package com.rs.battleship.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.rs.battleship.gui.GridDrawer;
import com.rs.battleship.main.Main;
import com.rs.battleship.settings.Settings;
import com.rs.battleship.sound.SoundPlayer;


@SuppressWarnings("serial")
public class Grid extends JPanel
{
	public static final Color DEFAULT_COLOR = Color.BLACK;
	public static final int WIDTH = Main.WIDTH / 2 - 8;
	
	private static String[][] LOCATIONS = 
	{
		{ "  ", "A ", "B ", "C ", "D ", "E ", "F ", "G ", "H ", "I ", "J " },
		{ " 1", "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1", "I1", "J1" },
		{ " 2", "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "I2", "J2" },
		{ " 3", "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", "I3", "J3" },
		{ " 4", "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4", "I4", "J4" },
		{ " 5", "A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5", "I5", "J5" },
		{ " 6", "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6", "I6", "J6" },
		{ " 7", "A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7", "I7", "J7" },
		{ " 8", "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", "I8", "J8" },
		{ " 9", "A9", "B9", "C9", "D9", "E9", "F9", "G9", "H9", "I9", "J9" },
		{ " 10", "A10", "B10", "C10", "D10", "E10", "F10", "G10", "H10", "I10", "J10" },
	};
	
	private Block[] grid = new Block[LOCATIONS.length * LOCATIONS.length];
	
	private GridDrawer drawer = new GridDrawer(this);
	
	public Grid()
	{
		this.setBackground(DEFAULT_COLOR);
		this.setLayout(null);
		
		addBlocks();
	}
	
	/**
	 * Returns true if location is a valid location on the grid
	 * @param location
	 */
	public static boolean isValidLocation(String location)
	{
		boolean isValid = false;
		
		for(int r=1; r<LOCATIONS.length; r++)
			for(int c=1; c<LOCATIONS[r].length; c++)
				if(LOCATIONS[r][c].equals(location))
					isValid = true;
		
		
		return isValid;
	}
	
	/**
	 * Returns row of location
	 * @param location
	 * @return row
	 */
	public static int getRow(String location)
	{
		String r = isValidLocation(location) ? " "+location.substring(1) : "invalid";
		int row = -1;
		
		for(int i=1; i<LOCATIONS.length; i++)
			if(LOCATIONS[i][0].equals(r))
				row = i;
		
		return row;
	}
	
	/**
	 * Returns column of the location
	 * @param location
	 * @return column
	 */
	public static int getCol(String location)
	{
		String c = isValidLocation(location) ? location.substring(0, 1)+" " : "invalid";
		int col = -1;
		
		for(int i=1; i<LOCATIONS[0].length; i++)
			if(LOCATIONS[0][i].equals(c))
				col = i;
		
		return col;
	}
	
	/**
	 * Returns location at (row, col)
	 * @param row
	 * @param col
	 * @return location
	 */
	public static String locationAt(int row, int col)
	{
		String returnVal;
		try{
			returnVal = LOCATIONS[row][col];
		} catch(ArrayIndexOutOfBoundsException e)
		{
			returnVal = "";
		}
		return returnVal;
	}
	
	/**
	 * Returns width of the Grid
	 * @return width
	 */
	public static int getGridSize()
	{
		return LOCATIONS.length;
	}
	
	/**
	 * Updates graphics on the Grid
	 */
	public void update()
	{
		this.repaint();
	}
	
	/**
	 * Overrides paint(Graphics g)
	 * Paints Ships and hits/misses
	 */
	public void paint(Graphics g)
	{
		super.paint(g);
		
		drawer.drawShips(g);
		drawer.drawHitsMisses(g);
		
	}
	
	/**
	 * Places a Ship at location, adds to GridDrawer
	 * to be drawn
	 * @param ship
	 * @param location
	 */
	public void placeShip(Ship s, String location)
	{		
		s.place(location);
		
		int row = Grid.getRow(s.getFirstBlock());
		int col = Grid.getCol(s.getFirstBlock());
		
		for(int i=0; i<s.getLength(); i++)
		{
			String block = s.getOrientation().equals("horizontal") ? Grid.locationAt(row, col + i) : Grid.locationAt(row + i, col);
			
			Block b = getBlockOn(block);
			b.placeShip(s.isEnemy());
		}
		
		if(!s.isEnemy())
		{
			drawer.addShip(s);
		}
	}
	
	/**
	 * Removes Ship from the Grid and GridDrawer
	 * @param ship
	 */
	public void removeShip(Ship s) 
	{
		s.remove();
		
		int row = Grid.getRow(s.getFirstBlock());
		int col = Grid.getCol(s.getFirstBlock());
		
		for(int i=0; i<s.getLength(); i++)
		{
			String block = s.getOrientation().equals("horizontal") ? Grid.locationAt(row, col + i) : Grid.locationAt(row + i, col);
			
			Block b = getBlockOn(block);
			b.removeShip();
		}
		drawer.removeShip(s);
		
		
	}
	
	/**
	 * Fires a shot on location at target Player, 
	 * updates Blocks and Ships
	 * @param location
	 * @param target
	 */
	public void fire(String location, Player target)
	{
		Block b = getBlockOn(location);
		if(isValidLocation(location))
		{
			if(!b.isOpen())
			{
				Ship targetShip = target.getShipOn(this, location);
				
				b.hit();
				targetShip.hit();
				
				if(Settings.soundOn())
				{
					new Thread()
					{
						public void run()
						{
							new SoundPlayer("hit.wav").play();
						}
					}.start();
				}
				
				
				if(targetShip.isDestroyed())
				{
					final String message = target.isEnemy() ? "You sunk the enemy's "+targetShip.toString().toUpperCase()+"!" 
							: "The enemy sunk your "+targetShip.toString().toUpperCase()+"!";
					
					//new thread so locations are sent quicker
					new Thread() {
						public void run()
						{
							JOptionPane.showMessageDialog(null,
							    message,
							    "Ship sunk!",
							    JOptionPane.WARNING_MESSAGE);
						}
					}.start();
				}
				drawer.addHit(b);
			}
			
			else
			{
				b.miss();
				
				if(Settings.soundOn())
				{
					new Thread()
					{
						public void run()
						{
							new SoundPlayer("miss.wav").play();
						}
					}.start();
				}
				
				drawer.addMiss(b);
					
			}
		}
	}
	
	/**
	 * Returns the Block on location
	 * @param location
	 * @return block
	 */
	public Block getBlockOn(String location)
	{
		Block blockOn = null;
		
		for(Block b : grid)
			if(b.getLoc().equals(location))
				blockOn = b;
		
		return blockOn;
	}
	
	/**
	 * Returns the Block at Point p
	 * @param p
	 * @return block
	 */
	public Block getBlockOn(Point p)
	{
		Block blockOn = null;
		
		for(Block b : grid)
			if(b.getBoundingBox().contains(p))
				blockOn = b;
		
		return blockOn;
	}
	
	/**
	 * Clears Ships from Blocks and GridDrawer
	 */
	public void clear()
	{
		for(Block b : grid)
			b.removeShip();
		
		drawer.reset();
	}
	
	/**
	 * Sets location and bounds for every Block
	 * on Grid
	 */
	public void addBlocks()
	{
		int count=0;
		for(String[] row : LOCATIONS)
			for(String loc : row)
			{
				Block b = new Block(loc);
				grid[count] = b;
				count++;
			}
		
		count = 0;
		for(int y=2; y<Block.WIDTH*11; y+=(Block.WIDTH+2))
			for(int x=2; x<Block.WIDTH*11; x+=(Block.WIDTH+2))
			{
				Rectangle box = new Rectangle(x, y, Block.WIDTH, Block.WIDTH);
				grid[count].setBounds(box);
				grid[count].setBoundingBox(box);
				this.add(grid[count]);
				
				count++;
			}
	}
}
